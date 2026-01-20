package ex.ecommerceserver.domain.product.application.dto;

import ex.ecommerceserver.domain.product.domain.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateRequest(
        @NotBlank(message = "상품명은 필수입니다")
        String name,

        String description,

        @NotNull(message = "가격은 필수입니다")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다")
        Integer price,

        @NotNull(message = "카테고리는 필수입니다")
        Category category
) {
}
