package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @Nested
    @DisplayName("OrderItem 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 OrderItem 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            Long productId = 1L;
            String productName = "테스트 상품";
            int price = 10000;
            int quantity = 2;

            // when
            OrderItem orderItem = OrderItem.create(productId, productName, price, quantity);

            // then
            assertThat(orderItem.getProductId()).isEqualTo(productId);
            assertThat(orderItem.getProductName()).isEqualTo(productName);
            assertThat(orderItem.getPrice()).isEqualTo(price);
            assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        }

        @Test
        @DisplayName("수량이 0이면 예외가 발생한다")
        void create_ZeroQuantity_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> OrderItem.create(1L, "상품", 10000, 0))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }

        @Test
        @DisplayName("수량이 음수이면 예외가 발생한다")
        void create_NegativeQuantity_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> OrderItem.create(1L, "상품", 10000, -1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }
    }

    @Nested
    @DisplayName("소계 계산")
    class GetSubtotal {

        @Test
        @DisplayName("소계가 올바르게 계산된다")
        void getSubtotal_ReturnsCorrectValue() {
            // given
            OrderItem orderItem = OrderItem.create(1L, "상품", 10000, 3);

            // when
            int subtotal = orderItem.getSubtotal();

            // then
            assertThat(subtotal).isEqualTo(30000);
        }
    }
}
