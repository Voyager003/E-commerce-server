package ex.ecommerceserver.domain.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ACTIVE("판매중"),
    INACTIVE("비활성"),
    OUT_OF_STOCK("품절"),
    DISCONTINUED("판매중단");

    private final String displayName;

    public boolean isAvailable() {
        return this == ACTIVE;
    }
}
