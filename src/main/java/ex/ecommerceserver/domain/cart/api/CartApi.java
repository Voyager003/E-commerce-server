package ex.ecommerceserver.domain.cart.api;

import ex.ecommerceserver.domain.cart.application.CartApplication;
import ex.ecommerceserver.domain.cart.application.dto.AddToCartRequest;
import ex.ecommerceserver.domain.cart.application.dto.CartResponse;
import ex.ecommerceserver.domain.cart.application.dto.UpdateCartItemRequest;
import ex.ecommerceserver.global.security.jwt.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartApi {

    private final CartApplication cartApplication;

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @LoginMember Long memberId,
            @Valid @RequestBody AddToCartRequest request
    ) {
        CartResponse response = cartApplication.addItem(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@LoginMember Long memberId) {
        CartResponse response = cartApplication.getCart(memberId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @LoginMember Long memberId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        CartResponse response = cartApplication.updateItemQuantity(memberId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @LoginMember Long memberId,
            @PathVariable Long itemId
    ) {
        cartApplication.removeItem(memberId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@LoginMember Long memberId) {
        cartApplication.clearCart(memberId);
        return ResponseEntity.noContent().build();
    }
}
