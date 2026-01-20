package ex.ecommerceserver.domain.order.application.dto;

import ex.ecommerceserver.domain.order.domain.Address;
import ex.ecommerceserver.domain.order.domain.Order;
import ex.ecommerceserver.domain.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long memberId,
        List<OrderItemResponse> items,
        AddressResponse shippingAddress,
        OrderStatus status,
        String statusDescription,
        int totalPrice,
        int itemCount,
        LocalDateTime orderedAt
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getMemberId(),
                itemResponses,
                AddressResponse.from(order.getShippingAddress()),
                order.getStatus(),
                order.getStatus().getDescription(),
                order.getTotalPrice(),
                order.getItemCount(),
                order.getOrderedAt()
        );
    }

    public record AddressResponse(
            String zipCode,
            String street,
            String detail,
            String receiverName,
            String receiverPhone,
            String fullAddress
    ) {
        public static AddressResponse from(Address address) {
            return new AddressResponse(
                    address.getZipCode(),
                    address.getStreet(),
                    address.getDetail(),
                    address.getReceiverName(),
                    address.getReceiverPhone(),
                    address.getFullAddress()
            );
        }
    }
}
