package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @Nested
    @DisplayName("Stock 생성")
    class Create {

        @Test
        @DisplayName("유효한 수량으로 Stock 객체를 생성할 수 있다")
        void of_ValidQuantity_Success() {
            // given
            int quantity = 100;

            // when
            Stock stock = Stock.of(quantity);

            // then
            assertThat(stock.quantity()).isEqualTo(quantity);
        }

        @Test
        @DisplayName("0개의 수량으로 Stock 객체를 생성할 수 있다")
        void of_ZeroQuantity_Success() {
            // given
            int quantity = 0;

            // when
            Stock stock = Stock.of(quantity);

            // then
            assertThat(stock.quantity()).isZero();
        }

        @Test
        @DisplayName("음수 수량으로 생성하면 예외가 발생한다")
        void of_NegativeQuantity_ThrowsException() {
            // given
            int quantity = -1;

            // when & then
            assertThatThrownBy(() -> Stock.of(quantity))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_STOCK);
        }
    }

    @Nested
    @DisplayName("재고 감소")
    class Decrease {

        @Test
        @DisplayName("유효한 수량만큼 재고를 감소시킬 수 있다")
        void decrease_ValidAmount_Success() {
            // given
            Stock stock = Stock.of(100);

            // when
            Stock decreased = stock.decrease(30);

            // then
            assertThat(decreased.quantity()).isEqualTo(70);
        }

        @Test
        @DisplayName("재고보다 많은 수량을 감소시키면 예외가 발생한다")
        void decrease_InsufficientStock_ThrowsException() {
            // given
            Stock stock = Stock.of(10);

            // when & then
            assertThatThrownBy(() -> stock.decrease(20))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INSUFFICIENT_STOCK);
        }

        @Test
        @DisplayName("음수 수량을 감소시키면 예외가 발생한다")
        void decrease_NegativeAmount_ThrowsException() {
            // given
            Stock stock = Stock.of(100);

            // when & then
            assertThatThrownBy(() -> stock.decrease(-10))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_STOCK);
        }
    }

    @Nested
    @DisplayName("재고 증가")
    class Increase {

        @Test
        @DisplayName("유효한 수량만큼 재고를 증가시킬 수 있다")
        void increase_ValidAmount_Success() {
            // given
            Stock stock = Stock.of(100);

            // when
            Stock increased = stock.increase(50);

            // then
            assertThat(increased.quantity()).isEqualTo(150);
        }

        @Test
        @DisplayName("음수 수량을 증가시키면 예외가 발생한다")
        void increase_NegativeAmount_ThrowsException() {
            // given
            Stock stock = Stock.of(100);

            // when & then
            assertThatThrownBy(() -> stock.increase(-10))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_STOCK);
        }
    }

    @Nested
    @DisplayName("재고 확인")
    class IsEmpty {

        @Test
        @DisplayName("재고가 0이면 isEmpty는 true를 반환한다")
        void isEmpty_ZeroQuantity_ReturnsTrue() {
            // given
            Stock stock = Stock.of(0);

            // when & then
            assertThat(stock.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("재고가 있으면 isEmpty는 false를 반환한다")
        void isEmpty_PositiveQuantity_ReturnsFalse() {
            // given
            Stock stock = Stock.of(10);

            // when & then
            assertThat(stock.isEmpty()).isFalse();
        }
    }
}
