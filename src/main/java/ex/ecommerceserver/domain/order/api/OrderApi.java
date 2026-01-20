package ex.ecommerceserver.domain.order.api;

import ex.ecommerceserver.domain.order.application.OrderApplication;
import ex.ecommerceserver.domain.order.application.dto.CreateOrderRequest;
import ex.ecommerceserver.domain.order.application.dto.OrderResponse;
import ex.ecommerceserver.domain.order.domain.OrderStatus;
import ex.ecommerceserver.global.security.jwt.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderApplication orderApplication;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @LoginMember Long memberId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = orderApplication.createOrder(memberId, request);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @LoginMember Long memberId,
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "orderedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderResponse> response;
        if (status != null) {
            response = orderApplication.getOrdersByStatus(memberId, status, pageable);
        } else {
            response = orderApplication.getOrders(memberId, pageable);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @LoginMember Long memberId,
            @PathVariable Long orderId
    ) {
        OrderResponse response = orderApplication.getOrder(memberId, orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @LoginMember Long memberId,
            @PathVariable Long orderId
    ) {
        OrderResponse response = orderApplication.cancelOrder(memberId, orderId);
        return ResponseEntity.ok(response);
    }
}
