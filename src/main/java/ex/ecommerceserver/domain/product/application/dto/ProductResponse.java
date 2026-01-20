package ex.ecommerceserver.domain.product.application.dto;

import ex.ecommerceserver.domain.product.domain.Category;
import ex.ecommerceserver.domain.product.domain.Product;
import ex.ecommerceserver.domain.product.domain.ProductStatus;

public record ProductResponse(
        Long id,
        String name,
        String description,
        int price,
        int stockQuantity,
        Category category,
        String categoryDisplayName,
        ProductStatus status,
        String statusDisplayName
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().value(),
                product.getStock().quantity(),
                product.getCategory(),
                product.getCategory().getDisplayName(),
                product.getStatus(),
                product.getStatus().getDisplayName()
        );
    }
}
