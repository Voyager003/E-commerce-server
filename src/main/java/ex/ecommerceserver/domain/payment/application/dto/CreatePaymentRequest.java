package ex.ecommerceserver.domain.payment.application.dto;

import ex.ecommerceserver.domain.payment.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull(message = "주문 ID는 필수입니다")
        Long orderId,

        @NotNull(message = "결제 수단은 필수입니다")
        PaymentMethod method
) {
}
