package ex.ecommerceserver.domain.cart.application.dto;

import ex.ecommerceserver.domain.cart.domain.CartItem;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        int price,
        int quantity,
        int subtotal
) {
    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProductId(),
                cartItem.getProductName(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getSubtotal()
        );
    }
}
