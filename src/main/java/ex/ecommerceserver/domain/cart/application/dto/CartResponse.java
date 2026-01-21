package ex.ecommerceserver.domain.cart.application.dto;

import ex.ecommerceserver.domain.cart.domain.Cart;

import java.util.List;

public record CartResponse(
        Long id,
        Long memberId,
        List<CartItemResponse> items,
        int totalPrice,
        int itemCount
) {
    public static CartResponse from(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getMemberId(),
                itemResponses,
                cart.getTotalPrice(),
                cart.getItemCount()
        );
    }
}
