package ex.ecommerceserver.domain.cart.entity;

import ex.ecommerceserver.domain.cart.domain.Cart;
import ex.ecommerceserver.domain.cart.domain.CartItem;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    private CartEntity(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }

    public static CartEntity from(Cart cart) {
        CartEntity entity = new CartEntity(cart.getId(), cart.getMemberId());
        cart.getItems().forEach(entity::addItem);
        return entity;
    }

    public Cart toDomain() {
        List<CartItem> cartItems = items.stream()
                .map(CartItemEntity::toDomain)
                .toList();

        return Cart.builder()
                .id(this.id)
                .memberId(this.memberId)
                .items(cartItems)
                .build();
    }

    public void addItem(CartItem cartItem) {
        CartItemEntity itemEntity = CartItemEntity.from(cartItem);
        itemEntity.setCart(this);
        this.items.add(itemEntity);
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
    }

    public void clearItems() {
        this.items.clear();
    }

    public CartItemEntity findItemById(Long itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public CartItemEntity findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
