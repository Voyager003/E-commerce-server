package ex.ecommerceserver.domain.product.entity;

import ex.ecommerceserver.domain.product.domain.*;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    private ProductEntity(Long id, String name, String description, int price,
                          int stockQuantity, Category category, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.status = status;
    }

    public static ProductEntity from(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().value(),
                product.getStock().quantity(),
                product.getCategory(),
                product.getStatus()
        );
    }

    public Product toDomain() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .price(Price.of(this.price))
                .stock(Stock.of(this.stockQuantity))
                .category(this.category)
                .status(this.status)
                .build();
    }

    public void update(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().value();
        this.stockQuantity = product.getStock().quantity();
        this.category = product.getCategory();
        this.status = product.getStatus();
    }
}
