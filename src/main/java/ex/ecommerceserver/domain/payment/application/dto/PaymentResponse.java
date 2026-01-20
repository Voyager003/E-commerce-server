package ex.ecommerceserver.domain.payment.application.dto;

import ex.ecommerceserver.domain.payment.domain.Payment;
import ex.ecommerceserver.domain.payment.domain.PaymentMethod;
import ex.ecommerceserver.domain.payment.domain.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentNumber,
        Long orderId,
        Long memberId,
        int amount,
        PaymentMethod method,
        String methodDescription,
        PaymentStatus status,
        String statusDescription,
        LocalDateTime createdAt,
        LocalDateTime paidAt,
        LocalDateTime cancelledAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentNumber(),
                payment.getOrderId(),
                payment.getMemberId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getMethod().getDescription(),
                payment.getStatus(),
                payment.getStatus().getDescription(),
                payment.getCreatedAt(),
                payment.getPaidAt(),
                payment.getCancelledAt()
        );
    }
}
