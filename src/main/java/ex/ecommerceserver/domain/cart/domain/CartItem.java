package ex.ecommerceserver.domain.cart.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItem {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final int price;
    private int quantity;

    @Builder
    private CartItem(Long id, Long productId, String productName, int price, int quantity) {
        validateQuantity(quantity);
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static CartItem create(Long productId, String productName, int price, int quantity) {
        return CartItem.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public void updateQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        validateQuantity(amount);
        this.quantity += amount;
    }

    public int getSubtotal() {
        return price * quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
    }
}
