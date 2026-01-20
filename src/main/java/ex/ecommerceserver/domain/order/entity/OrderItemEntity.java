package ex.ecommerceserver.domain.order.entity;

import ex.ecommerceserver.domain.order.domain.OrderItem;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemEntity extends BaseTimeEntity {

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
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private OrderItemEntity(Long id, Long productId, String productName, int price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemEntity from(OrderItem orderItem) {
        return new OrderItemEntity(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getPrice(),
                orderItem.getQuantity()
        );
    }

    public OrderItem toDomain() {
        return OrderItem.builder()
                .id(this.id)
                .productId(this.productId)
                .productName(this.productName)
                .price(this.price)
                .quantity(this.quantity)
                .build();
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}
