package ex.ecommerceserver.domain.product.api;

import ex.ecommerceserver.domain.product.application.AdminProductApplication;
import ex.ecommerceserver.domain.product.application.dto.*;
import ex.ecommerceserver.domain.product.domain.Category;
import ex.ecommerceserver.domain.product.domain.ProductStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductApi {

    private final AdminProductApplication adminProductApplication;

    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) ProductStatus status,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(adminProductApplication.getProducts(category, status, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(adminProductApplication.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse response = adminProductApplication.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return ResponseEntity.ok(adminProductApplication.updateProduct(productId, request));
    }

    @PostMapping("/{productId}/stock/increase")
    public ResponseEntity<Void> increaseStock(
            @PathVariable Long productId,
            @Valid @RequestBody ProductStockRequest request
    ) {
        adminProductApplication.increaseStock(productId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/stock/decrease")
    public ResponseEntity<Void> decreaseStock(
            @PathVariable Long productId,
            @Valid @RequestBody ProductStockRequest request
    ) {
        adminProductApplication.decreaseStock(productId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminProductApplication.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
