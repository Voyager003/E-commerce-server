package ex.ecommerceserver.domain.product.application;

import ex.ecommerceserver.domain.product.application.dto.*;
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
public class AdminProductApplication {

    private final ProductRepository productRepository;

    public ProductListResponse getProducts(Category category, ProductStatus status, Pageable pageable) {
        Page<ProductResponse> products = productRepository.findByFilters(category, status, pageable)
                .map(ProductEntity::toDomain)
                .map(ProductResponse::from);
        return ProductListResponse.from(products);
    }

    public ProductResponse getProduct(Long productId) {
        Product product = findProductById(productId);
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        validateDuplicateName(request.name());

        Product product = Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                request.category()
        );

        ProductEntity savedEntity = productRepository.save(ProductEntity.from(product));
        return ProductResponse.from(savedEntity.toDomain());
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {
        ProductEntity entity = findEntityById(productId);
        Product product = entity.toDomain();

        if (!product.getName().equals(request.name())) {
            validateDuplicateName(request.name());
        }

        product.update(request.name(), request.description(), request.price(), request.category());
        entity.update(product);

        return ProductResponse.from(product);
    }

    @Transactional
    public void increaseStock(Long productId, ProductStockRequest request) {
        ProductEntity entity = findEntityByIdWithLock(productId);
        Product product = entity.toDomain();

        product.increaseStock(request.quantity());
        entity.update(product);
    }

    @Transactional
    public void decreaseStock(Long productId, ProductStockRequest request) {
        ProductEntity entity = findEntityByIdWithLock(productId);
        Product product = entity.toDomain();

        product.decreaseStock(request.quantity());
        entity.update(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductEntity entity = findEntityById(productId);
        Product product = entity.toDomain();

        product.discontinue();
        entity.update(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductEntity::toDomain)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductEntity findEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductEntity findEntityByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void validateDuplicateName(String name) {
        if (productRepository.existsByName(name)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PRODUCT_NAME);
        }
    }
}
