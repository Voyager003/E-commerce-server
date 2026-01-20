package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private final Long id;
    private final String orderNumber;
    private final Long memberId;
    private final List<OrderItem> items;
    private final Address shippingAddress;
    private OrderStatus status;
    private final LocalDateTime orderedAt;

    @Builder
    private Order(Long id, String orderNumber, Long memberId, List<OrderItem> items,
                  Address shippingAddress, OrderStatus status, LocalDateTime orderedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.memberId = memberId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public static Order create(Long memberId, List<OrderItem> items, Address shippingAddress) {
        validateItems(items);
        return Order.builder()
                .orderNumber(generateOrderNumber())
                .memberId(memberId)
                .items(items)
                .shippingAddress(shippingAddress)
                .status(OrderStatus.PENDING)
                .orderedAt(LocalDateTime.now())
                .build();
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void prepare() {
        this.status = OrderStatus.PREPARING;
    }

    public void ship() {
        this.status = OrderStatus.SHIPPING;
    }

    public void deliver() {
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if (!status.isCancellable()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }
        this.status = OrderStatus.CANCELLED;
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(OrderItem::getSubtotal)
                .sum();
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    private static void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_ORDER_ITEMS);
        }
    }

    private static String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
