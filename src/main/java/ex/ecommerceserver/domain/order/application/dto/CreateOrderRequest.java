package ex.ecommerceserver.domain.order.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "주문 항목은 필수입니다")
        @Valid
        List<OrderItemRequest> items,

        @NotNull(message = "배송지 정보는 필수입니다")
        @Valid
        AddressRequest shippingAddress
) {
}
