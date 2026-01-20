package ex.ecommerceserver.domain.product.application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ProductListResponse(
        List<ProductResponse> products,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
    public static ProductListResponse from(Page<ProductResponse> productPage) {
        return new ProductListResponse(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.hasNext(),
                productPage.hasPrevious()
        );
    }
}
