package ex.ecommerceserver.domain.order.application.dto;

import ex.ecommerceserver.domain.order.domain.OrderItem;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        int price,
        int quantity,
        int subtotal
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getSubtotal()
        );
    }
}
