package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private Address createTestAddress() {
        return Address.of("12345", "서울시 강남구", "101동", "홍길동", "010-1234-5678");
    }

    private List<OrderItem> createTestItems() {
        return List.of(
                OrderItem.create(1L, "상품1", 10000, 2),
                OrderItem.create(2L, "상품2", 5000, 3)
        );
    }

    @Nested
    @DisplayName("Order 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 Order 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            Long memberId = 1L;
            List<OrderItem> items = createTestItems();
            Address address = createTestAddress();

            // when
            Order order = Order.create(memberId, items, address);

            // then
            assertThat(order.getMemberId()).isEqualTo(memberId);
            assertThat(order.getItems()).hasSize(2);
            assertThat(order.getShippingAddress()).isEqualTo(address);
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(order.getOrderNumber()).startsWith("ORD-");
            assertThat(order.getOrderedAt()).isNotNull();
        }

        @Test
        @DisplayName("주문 항목이 null이면 예외가 발생한다")
        void create_NullItems_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Order.create(1L, null, createTestAddress()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EMPTY_ORDER_ITEMS);
        }

        @Test
        @DisplayName("주문 항목이 비어있으면 예외가 발생한다")
        void create_EmptyItems_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Order.create(1L, new ArrayList<>(), createTestAddress()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EMPTY_ORDER_ITEMS);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class StatusChange {

        @Test
        @DisplayName("PENDING 상태에서 취소할 수 있다")
        void cancel_PendingOrder_Success() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

            // when
            order.cancel();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        }

        @Test
        @DisplayName("CONFIRMED 상태에서 취소할 수 있다")
        void cancel_ConfirmedOrder_Success() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());
            order.confirm();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);

            // when
            order.cancel();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        }

        @Test
        @DisplayName("PREPARING 상태에서는 취소할 수 없다")
        void cancel_PreparingOrder_ThrowsException() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());
            order.confirm();
            order.prepare();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PREPARING);

            // when & then
            assertThatThrownBy(order::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }

        @Test
        @DisplayName("SHIPPING 상태에서는 취소할 수 없다")
        void cancel_ShippingOrder_ThrowsException() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());
            order.confirm();
            order.prepare();
            order.ship();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPING);

            // when & then
            assertThatThrownBy(order::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }

        @Test
        @DisplayName("DELIVERED 상태에서는 취소할 수 없다")
        void cancel_DeliveredOrder_ThrowsException() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());
            order.confirm();
            order.prepare();
            order.ship();
            order.deliver();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);

            // when & then
            assertThatThrownBy(order::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }

        @Test
        @DisplayName("상태 흐름을 따라 변경할 수 있다")
        void statusFlow_FullFlow_Success() {
            // given
            Order order = Order.create(1L, createTestItems(), createTestAddress());

            // when & then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

            order.confirm();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);

            order.prepare();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PREPARING);

            order.ship();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPING);

            order.deliver();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        }
    }

    @Nested
    @DisplayName("총액 계산")
    class GetTotalPrice {

        @Test
        @DisplayName("총액이 올바르게 계산된다")
        void getTotalPrice_MultipleItems_ReturnsCorrectTotal() {
            // given
            List<OrderItem> items = createTestItems(); // 10000*2 + 5000*3 = 35000
            Order order = Order.create(1L, items, createTestAddress());

            // when
            int totalPrice = order.getTotalPrice();

            // then
            assertThat(totalPrice).isEqualTo(35000);
        }
    }

    @Nested
    @DisplayName("주문 항목 수 조회")
    class GetItemCount {

        @Test
        @DisplayName("주문 항목 수를 올바르게 반환한다")
        void getItemCount_ReturnsCorrectCount() {
            // given
            List<OrderItem> items = createTestItems();
            Order order = Order.create(1L, items, createTestAddress());

            // when
            int itemCount = order.getItemCount();

            // then
            assertThat(itemCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("총 수량 조회")
    class GetTotalQuantity {

        @Test
        @DisplayName("총 수량을 올바르게 반환한다")
        void getTotalQuantity_ReturnsCorrectQuantity() {
            // given
            List<OrderItem> items = createTestItems(); // 2 + 3 = 5
            Order order = Order.create(1L, items, createTestAddress());

            // when
            int totalQuantity = order.getTotalQuantity();

            // then
            assertThat(totalQuantity).isEqualTo(5);
        }
    }
}
