package ex.ecommerceserver.domain.cart.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Cart {
    private final Long id;
    private final Long memberId;
    private final List<CartItem> items;

    @Builder
    private Cart(Long id, Long memberId, List<CartItem> items) {
        this.id = id;
        this.memberId = memberId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public static Cart create(Long memberId) {
        return Cart.builder()
                .memberId(memberId)
                .items(new ArrayList<>())
                .build();
    }

    public void addItem(CartItem item) {
        Optional<CartItem> existingItem = findItemByProductId(item.getProductId());
        if (existingItem.isPresent()) {
            existingItem.get().addQuantity(item.getQuantity());
        } else {
            items.add(item);
        }
    }

    public void updateItemQuantity(Long itemId, int quantity) {
        CartItem item = findItemById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        item.updateQuantity(quantity);
    }

    public void removeItem(Long itemId) {
        CartItem item = findItemById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(CartItem::getSubtotal)
                .sum();
    }

    public Optional<CartItem> findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    public Optional<CartItem> findItemById(Long itemId) {
        return items.stream()
                .filter(item -> item.getId() != null && item.getId().equals(itemId))
                .findFirst();
    }

    public int getItemCount() {
        return items.size();
    }
}
