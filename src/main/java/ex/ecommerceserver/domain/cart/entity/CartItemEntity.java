package ex.ecommerceserver.domain.cart.entity;

import ex.ecommerceserver.domain.cart.domain.CartItem;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    private CartItemEntity(Long id, Long productId, String productName, int price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static CartItemEntity from(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getProductId(),
                cartItem.getProductName(),
                cartItem.getPrice(),
                cartItem.getQuantity()
        );
    }

    public CartItem toDomain() {
        return CartItem.builder()
                .id(this.id)
                .productId(this.productId)
                .productName(this.productName)
                .price(this.price)
                .quantity(this.quantity)
                .build();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }
}
