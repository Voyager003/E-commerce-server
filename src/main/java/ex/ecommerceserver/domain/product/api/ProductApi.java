package ex.ecommerceserver.domain.product.api;

import ex.ecommerceserver.domain.product.application.ProductApplication;
import ex.ecommerceserver.domain.product.application.dto.ProductListResponse;
import ex.ecommerceserver.domain.product.application.dto.ProductResponse;
import ex.ecommerceserver.domain.product.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductApplication productApplication;

    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(required = false) Category category,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(productApplication.getActiveProducts(category, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productApplication.getProduct(productId));
    }
}
