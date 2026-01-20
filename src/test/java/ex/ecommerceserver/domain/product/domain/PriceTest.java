package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Nested
    @DisplayName("Price 생성")
    class Create {

        @Test
        @DisplayName("유효한 가격으로 Price 객체를 생성할 수 있다")
        void of_ValidPrice_Success() {
            // given
            int value = 10000;

            // when
            Price price = Price.of(value);

            // then
            assertThat(price.value()).isEqualTo(value);
        }

        @Test
        @DisplayName("0원으로 Price 객체를 생성할 수 있다")
        void of_ZeroPrice_Success() {
            // given
            int value = 0;

            // when
            Price price = Price.of(value);

            // then
            assertThat(price.value()).isZero();
        }

        @Test
        @DisplayName("음수 가격으로 생성하면 예외가 발생한다")
        void of_NegativePrice_ThrowsException() {
            // given
            int value = -1000;

            // when & then
            assertThatThrownBy(() -> Price.of(value))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PRICE);
        }

        @Test
        @DisplayName("record의 생성자로 Price 객체를 생성할 수 있다")
        void constructor_ValidPrice_Success() {
            // given
            int value = 5000;

            // when
            Price price = new Price(value);

            // then
            assertThat(price.value()).isEqualTo(value);
        }
    }
}
