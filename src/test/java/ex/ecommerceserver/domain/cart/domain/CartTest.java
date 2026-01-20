package ex.ecommerceserver.domain.cart.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    @Nested
    @DisplayName("Cart 생성")
    class Create {

        @Test
        @DisplayName("회원 ID로 빈 장바구니를 생성할 수 있다")
        void create_ValidMemberId_Success() {
            // given
            Long memberId = 1L;

            // when
            Cart cart = Cart.create(memberId);

            // then
            assertThat(cart.getMemberId()).isEqualTo(memberId);
            assertThat(cart.getItems()).isEmpty();
            assertThat(cart.getItemCount()).isZero();
        }
    }

    @Nested
    @DisplayName("항목 추가")
    class AddItem {

        @Test
        @DisplayName("장바구니에 새 상품을 추가할 수 있다")
        void addItem_NewProduct_Success() {
            // given
            Cart cart = Cart.create(1L);
            CartItem item = CartItem.create(100L, "테스트 상품", 10000, 2);

            // when
            cart.addItem(item);

            // then
            assertThat(cart.getItems()).hasSize(1);
            assertThat(cart.getItems().get(0).getProductId()).isEqualTo(100L);
            assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("이미 있는 상품을 추가하면 수량이 증가한다")
        void addItem_ExistingProduct_QuantityIncreased() {
            // given
            Cart cart = Cart.create(1L);
            CartItem item1 = CartItem.builder()
                    .id(1L)
                    .productId(100L)
                    .productName("테스트 상품")
                    .price(10000)
                    .quantity(2)
                    .build();
            cart.addItem(item1);

            CartItem item2 = CartItem.create(100L, "테스트 상품", 10000, 3);

            // when
            cart.addItem(item2);

            // then
            assertThat(cart.getItems()).hasSize(1);
            assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("다른 상품을 추가하면 별도 항목으로 추가된다")
        void addItem_DifferentProduct_AddedSeparately() {
            // given
            Cart cart = Cart.create(1L);
            CartItem item1 = CartItem.create(100L, "상품1", 10000, 2);
            CartItem item2 = CartItem.create(200L, "상품2", 20000, 1);

            // when
            cart.addItem(item1);
            cart.addItem(item2);

            // then
            assertThat(cart.getItems()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("수량 변경")
    class UpdateItemQuantity {

        @Test
        @DisplayName("항목의 수량을 변경할 수 있다")
        void updateItemQuantity_ValidItemId_Success() {
            // given
            Cart cart = Cart.create(1L);
            CartItem item = CartItem.builder()
                    .id(1L)
                    .productId(100L)
                    .productName("상품")
                    .price(10000)
                    .quantity(2)
                    .build();
            cart.addItem(item);

            // when
            cart.updateItemQuantity(1L, 5);

            // then
            assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("존재하지 않는 항목의 수량을 변경하면 예외가 발생한다")
        void updateItemQuantity_InvalidItemId_ThrowsException() {
            // given
            Cart cart = Cart.create(1L);

            // when & then
            assertThatThrownBy(() -> cart.updateItemQuantity(999L, 5))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("항목 삭제")
    class RemoveItem {

        @Test
        @DisplayName("장바구니에서 항목을 삭제할 수 있다")
        void removeItem_ValidItemId_Success() {
            // given
            Cart cart = Cart.create(1L);
            CartItem item = CartItem.builder()
                    .id(1L)
                    .productId(100L)
                    .productName("상품")
                    .price(10000)
                    .quantity(2)
                    .build();
            cart.addItem(item);

            // when
            cart.removeItem(1L);

            // then
            assertThat(cart.getItems()).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 항목을 삭제하면 예외가 발생한다")
        void removeItem_InvalidItemId_ThrowsException() {
            // given
            Cart cart = Cart.create(1L);

            // when & then
            assertThatThrownBy(() -> cart.removeItem(999L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("장바구니 비우기")
    class Clear {

        @Test
        @DisplayName("장바구니의 모든 항목을 삭제할 수 있다")
        void clear_ItemsExist_AllItemsRemoved() {
            // given
            Cart cart = Cart.create(1L);
            cart.addItem(CartItem.create(100L, "상품1", 10000, 2));
            cart.addItem(CartItem.create(200L, "상품2", 20000, 1));

            // when
            cart.clear();

            // then
            assertThat(cart.getItems()).isEmpty();
            assertThat(cart.getItemCount()).isZero();
        }
    }

    @Nested
    @DisplayName("총액 계산")
    class GetTotalPrice {

        @Test
        @DisplayName("장바구니 총액이 올바르게 계산된다")
        void getTotalPrice_MultipleItems_ReturnsCorrectTotal() {
            // given
            Cart cart = Cart.create(1L);
            cart.addItem(CartItem.create(100L, "상품1", 10000, 2));  // 20000
            cart.addItem(CartItem.create(200L, "상품2", 5000, 3));   // 15000

            // when
            int totalPrice = cart.getTotalPrice();

            // then
            assertThat(totalPrice).isEqualTo(35000);
        }

        @Test
        @DisplayName("빈 장바구니의 총액은 0이다")
        void getTotalPrice_EmptyCart_ReturnsZero() {
            // given
            Cart cart = Cart.create(1L);

            // when
            int totalPrice = cart.getTotalPrice();

            // then
            assertThat(totalPrice).isZero();
        }
    }

    @Nested
    @DisplayName("상품으로 항목 찾기")
    class FindItemByProductId {

        @Test
        @DisplayName("상품 ID로 장바구니 항목을 찾을 수 있다")
        void findItemByProductId_ExistingProduct_ReturnsItem() {
            // given
            Cart cart = Cart.create(1L);
            cart.addItem(CartItem.create(100L, "테스트 상품", 10000, 2));

            // when
            var result = cart.findItemByProductId(100L);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getProductId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("존재하지 않는 상품 ID로 찾으면 빈 결과를 반환한다")
        void findItemByProductId_NonExistingProduct_ReturnsEmpty() {
            // given
            Cart cart = Cart.create(1L);

            // when
            var result = cart.findItemByProductId(999L);

            // then
            assertThat(result).isEmpty();
        }
    }
}
