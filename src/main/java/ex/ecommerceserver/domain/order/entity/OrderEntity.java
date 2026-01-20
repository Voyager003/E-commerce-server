package ex.ecommerceserver.domain.order.entity;

import ex.ecommerceserver.domain.order.domain.Address;
import ex.ecommerceserver.domain.order.domain.Order;
import ex.ecommerceserver.domain.order.domain.OrderItem;
import ex.ecommerceserver.domain.order.domain.OrderStatus;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String street;

    private String addressDetail;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    private OrderEntity(Long id, String orderNumber, Long memberId, OrderStatus status,
                        String zipCode, String street, String addressDetail,
                        String receiverName, String receiverPhone, LocalDateTime orderedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.memberId = memberId;
        this.status = status;
        this.zipCode = zipCode;
        this.street = street;
        this.addressDetail = addressDetail;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.orderedAt = orderedAt;
    }

    public static OrderEntity from(Order order) {
        OrderEntity entity = new OrderEntity(
                order.getId(),
                order.getOrderNumber(),
                order.getMemberId(),
                order.getStatus(),
                order.getShippingAddress().getZipCode(),
                order.getShippingAddress().getStreet(),
                order.getShippingAddress().getDetail(),
                order.getShippingAddress().getReceiverName(),
                order.getShippingAddress().getReceiverPhone(),
                order.getOrderedAt()
        );
        order.getItems().forEach(entity::addItem);
        return entity;
    }

    public Order toDomain() {
        List<OrderItem> orderItems = items.stream()
                .map(OrderItemEntity::toDomain)
                .toList();

        Address address = Address.of(
                this.zipCode,
                this.street,
                this.addressDetail,
                this.receiverName,
                this.receiverPhone
        );

        return Order.builder()
                .id(this.id)
                .orderNumber(this.orderNumber)
                .memberId(this.memberId)
                .items(orderItems)
                .shippingAddress(address)
                .status(this.status)
                .orderedAt(this.orderedAt)
                .build();
    }

    public void addItem(OrderItem orderItem) {
        OrderItemEntity itemEntity = OrderItemEntity.from(orderItem);
        itemEntity.setOrder(this);
        this.items.add(itemEntity);
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
