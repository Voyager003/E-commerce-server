package ex.ecommerceserver.domain.cart.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    @Nested
    @DisplayName("CartItem 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 CartItem 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            Long productId = 1L;
            String productName = "테스트 상품";
            int price = 10000;
            int quantity = 2;

            // when
            CartItem cartItem = CartItem.create(productId, productName, price, quantity);

            // then
            assertThat(cartItem.getProductId()).isEqualTo(productId);
            assertThat(cartItem.getProductName()).isEqualTo(productName);
            assertThat(cartItem.getPrice()).isEqualTo(price);
            assertThat(cartItem.getQuantity()).isEqualTo(quantity);
        }

        @Test
        @DisplayName("수량이 0이면 예외가 발생한다")
        void create_ZeroQuantity_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartItem.create(1L, "상품", 10000, 0))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }

        @Test
        @DisplayName("수량이 음수이면 예외가 발생한다")
        void create_NegativeQuantity_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> CartItem.create(1L, "상품", 10000, -1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }
    }

    @Nested
    @DisplayName("수량 변경")
    class UpdateQuantity {

        @Test
        @DisplayName("수량을 변경할 수 있다")
        void updateQuantity_ValidQuantity_Success() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 2);

            // when
            cartItem.updateQuantity(5);

            // then
            assertThat(cartItem.getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("수량이 0이면 예외가 발생한다")
        void updateQuantity_ZeroQuantity_ThrowsException() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 2);

            // when & then
            assertThatThrownBy(() -> cartItem.updateQuantity(0))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }

        @Test
        @DisplayName("수량이 음수이면 예외가 발생한다")
        void updateQuantity_NegativeQuantity_ThrowsException() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 2);

            // when & then
            assertThatThrownBy(() -> cartItem.updateQuantity(-1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_QUANTITY);
        }
    }

    @Nested
    @DisplayName("수량 추가")
    class AddQuantity {

        @Test
        @DisplayName("수량을 추가할 수 있다")
        void addQuantity_ValidAmount_Success() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 2);

            // when
            cartItem.addQuantity(3);

            // then
            assertThat(cartItem.getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("추가할 수량이 0이면 예외가 발생한다")
        void addQuantity_ZeroAmount_ThrowsException() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 2);

            // when & then
            assertThatThrownBy(() -> cartItem.addQuantity(0))
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
            CartItem cartItem = CartItem.create(1L, "상품", 10000, 3);

            // when
            int subtotal = cartItem.getSubtotal();

            // then
            assertThat(subtotal).isEqualTo(30000);
        }

        @Test
        @DisplayName("수량 변경 후 소계가 올바르게 계산된다")
        void getSubtotal_AfterQuantityUpdate_ReturnsCorrectValue() {
            // given
            CartItem cartItem = CartItem.create(1L, "상품", 5000, 2);
            cartItem.updateQuantity(4);

            // when
            int subtotal = cartItem.getSubtotal();

            // then
            assertThat(subtotal).isEqualTo(20000);
        }
    }
}
