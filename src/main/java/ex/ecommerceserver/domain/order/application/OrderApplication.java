package ex.ecommerceserver.domain.order.application;

import ex.ecommerceserver.domain.order.application.dto.CreateOrderRequest;
import ex.ecommerceserver.domain.order.application.dto.OrderItemRequest;
import ex.ecommerceserver.domain.order.application.dto.OrderResponse;
import ex.ecommerceserver.domain.order.domain.Address;
import ex.ecommerceserver.domain.order.domain.Order;
import ex.ecommerceserver.domain.order.domain.OrderItem;
import ex.ecommerceserver.domain.order.domain.OrderStatus;
import ex.ecommerceserver.domain.order.entity.OrderEntity;
import ex.ecommerceserver.domain.order.repository.OrderRepository;
import ex.ecommerceserver.domain.product.domain.Product;
import ex.ecommerceserver.domain.product.entity.ProductEntity;
import ex.ecommerceserver.domain.product.repository.ProductRepository;
import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderApplication {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(Long memberId, CreateOrderRequest request) {
        List<OrderItem> orderItems = createOrderItems(request.items());
        Address shippingAddress = request.shippingAddress().toDomain();

        Order order = Order.create(memberId, orderItems, shippingAddress);

        decreaseProductStock(request.items());

        OrderEntity savedEntity = orderRepository.save(OrderEntity.from(order));
        return OrderResponse.from(savedEntity.toDomain());
    }

    public Page<OrderResponse> getOrders(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberId(memberId, pageable)
                .map(entity -> OrderResponse.from(entity.toDomain()));
    }

    public Page<OrderResponse> getOrdersByStatus(Long memberId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByMemberIdAndStatus(memberId, status, pageable)
                .map(entity -> OrderResponse.from(entity.toDomain()));
    }

    public OrderResponse getOrder(Long memberId, Long orderId) {
        OrderEntity orderEntity = orderRepository.findByIdAndMemberIdWithItems(orderId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        return OrderResponse.from(orderEntity.toDomain());
    }

    @Transactional
    public OrderResponse cancelOrder(Long memberId, Long orderId) {
        OrderEntity orderEntity = orderRepository.findByIdAndMemberIdWithItems(orderId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        Order order = orderEntity.toDomain();
        order.cancel();

        restoreProductStock(order.getItems());

        orderEntity.updateStatus(OrderStatus.CANCELLED);
        return OrderResponse.from(orderEntity.toDomain());
    }

    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = getProduct(itemRequest.productId());
            validateProductAvailable(product);
            validateStock(product, itemRequest.quantity());

            OrderItem orderItem = OrderItem.create(
                    product.getId(),
                    product.getName(),
                    product.getPrice().value(),
                    itemRequest.quantity()
            );
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private void decreaseProductStock(List<OrderItemRequest> itemRequests) {
        for (OrderItemRequest itemRequest : itemRequests) {
            ProductEntity productEntity = productRepository.findByIdWithLock(itemRequest.productId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

            Product product = productEntity.toDomain();
            product.decreaseStock(itemRequest.quantity());
            productEntity.update(product);
        }
    }

    private void restoreProductStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            ProductEntity productEntity = productRepository.findByIdWithLock(item.getProductId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

            Product product = productEntity.toDomain();
            product.increaseStock(item.getQuantity());
            productEntity.update(product);
        }
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

    private void validateStock(Product product, int quantity) {
        if (product.getStock().quantity() < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }
}
