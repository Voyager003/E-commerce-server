package ex.ecommerceserver.domain.cart.application;

import ex.ecommerceserver.domain.cart.application.dto.AddToCartRequest;
import ex.ecommerceserver.domain.cart.application.dto.CartResponse;
import ex.ecommerceserver.domain.cart.application.dto.UpdateCartItemRequest;
import ex.ecommerceserver.domain.cart.domain.Cart;
import ex.ecommerceserver.domain.cart.domain.CartItem;
import ex.ecommerceserver.domain.cart.entity.CartEntity;
import ex.ecommerceserver.domain.cart.entity.CartItemEntity;
import ex.ecommerceserver.domain.cart.repository.CartRepository;
import ex.ecommerceserver.domain.product.domain.Product;
import ex.ecommerceserver.domain.product.entity.ProductEntity;
import ex.ecommerceserver.domain.product.repository.ProductRepository;
import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartApplication {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse addItem(Long memberId, AddToCartRequest request) {
        Product product = getProduct(request.productId());
        validateProductAvailable(product);

        CartEntity cartEntity = getOrCreateCart(memberId);
        CartItemEntity existingItem = cartEntity.findItemByProductId(request.productId());

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + request.quantity());
        } else {
            CartItem newItem = CartItem.create(
                    product.getId(),
                    product.getName(),
                    product.getPrice().value(),
                    request.quantity()
            );
            cartEntity.addItem(newItem);
        }

        return CartResponse.from(cartEntity.toDomain());
    }

    public CartResponse getCart(Long memberId) {
        CartEntity cartEntity = cartRepository.findByMemberIdWithItems(memberId)
                .orElseGet(() -> CartEntity.from(Cart.create(memberId)));

        return CartResponse.from(cartEntity.toDomain());
    }

    @Transactional
    public CartResponse updateItemQuantity(Long memberId, Long itemId, UpdateCartItemRequest request) {
        CartEntity cartEntity = getCartEntity(memberId);
        CartItemEntity itemEntity = cartEntity.findItemById(itemId);

        if (itemEntity == null) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        itemEntity.updateQuantity(request.quantity());
        return CartResponse.from(cartEntity.toDomain());
    }

    @Transactional
    public void removeItem(Long memberId, Long itemId) {
        CartEntity cartEntity = getCartEntity(memberId);
        CartItemEntity itemEntity = cartEntity.findItemById(itemId);

        if (itemEntity == null) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        cartEntity.removeItem(itemId);
    }

    @Transactional
    public void clearCart(Long memberId) {
        cartRepository.findByMemberIdWithItems(memberId)
                .ifPresent(CartEntity::clearItems);
    }

    private CartEntity getOrCreateCart(Long memberId) {
        return cartRepository.findByMemberIdWithItems(memberId)
                .orElseGet(() -> cartRepository.save(CartEntity.from(Cart.create(memberId))));
    }

    private CartEntity getCartEntity(Long memberId) {
        return cartRepository.findByMemberIdWithItems(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
    }

    private Product getProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        return productEntity.toDomain();
    }

    private void validateProductAvailable(Product product) {
        if (!product.isAvailable()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }
    }
}
