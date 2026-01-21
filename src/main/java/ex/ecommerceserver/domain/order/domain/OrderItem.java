package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItem {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final int price;
    private final int quantity;

    @Builder
    private OrderItem(Long id, Long productId, String productName, int price, int quantity) {
        validateQuantity(quantity);
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(Long productId, String productName, int price, int quantity) {
        return OrderItem.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build();
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
