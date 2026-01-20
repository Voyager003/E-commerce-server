package ex.ecommerceserver.domain.product.application;

import ex.ecommerceserver.domain.product.application.dto.ProductListResponse;
import ex.ecommerceserver.domain.product.application.dto.ProductResponse;
import ex.ecommerceserver.domain.product.domain.Category;
import ex.ecommerceserver.domain.product.domain.Product;
import ex.ecommerceserver.domain.product.domain.ProductStatus;
import ex.ecommerceserver.domain.product.entity.ProductEntity;
import ex.ecommerceserver.domain.product.repository.ProductRepository;
import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductApplication {

    private final ProductRepository productRepository;

    public ProductListResponse getActiveProducts(Category category, Pageable pageable) {
        Page<ProductResponse> products;
        if (category != null) {
            products = productRepository.findByCategoryAndStatus(category, ProductStatus.ACTIVE, pageable)
                    .map(ProductEntity::toDomain)
                    .map(ProductResponse::from);
        } else {
            products = productRepository.findByStatus(ProductStatus.ACTIVE, pageable)
                    .map(ProductEntity::toDomain)
                    .map(ProductResponse::from);
        }
        return ProductListResponse.from(products);
    }

    public ProductResponse getProduct(Long productId) {
        Product product = findProductById(productId);
        if (!product.isAvailable()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }
        return ProductResponse.from(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductEntity::toDomain)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
