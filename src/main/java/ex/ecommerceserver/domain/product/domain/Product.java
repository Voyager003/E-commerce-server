package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {
    private final Long id;
    private String name;
    private String description;
    private Price price;
    private Stock stock;
    private Category category;
    private ProductStatus status;

    @Builder
    private Product(Long id, String name, String description, Price price, Stock stock,
                    Category category, ProductStatus status) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.status = status;
    }

    public static Product create(String name, String description, int price,
                                  int stockQuantity, Category category) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(Price.of(price))
                .stock(Stock.of(stockQuantity))
                .category(category)
                .status(ProductStatus.ACTIVE)
                .build();
    }

    public void update(String name, String description, int price, Category category) {
        validateName(name);
        this.name = name;
        this.description = description;
        this.price = Price.of(price);
        this.category = category;
    }

    public void decreaseStock(int amount) {
        this.stock = this.stock.decrease(amount);
        if (this.stock.isEmpty()) {
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    public void increaseStock(int amount) {
        this.stock = this.stock.increase(amount);
        if (this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    public void activate() {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new BusinessException(ErrorCode.CANNOT_ACTIVATE_DISCONTINUED_PRODUCT);
        }
        this.status = ProductStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    public boolean isAvailable() {
        return this.status.isAvailable();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_NAME);
        }
    }
}
