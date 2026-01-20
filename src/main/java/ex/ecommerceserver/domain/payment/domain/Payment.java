package ex.ecommerceserver.domain.payment.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Payment {
    private final Long id;
    private final String paymentNumber;
    private final Long orderId;
    private final Long memberId;
    private final int amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    @Builder
    private Payment(Long id, String paymentNumber, Long orderId, Long memberId,
                    int amount, PaymentMethod method, PaymentStatus status,
                    LocalDateTime createdAt, LocalDateTime paidAt, LocalDateTime cancelledAt) {
        this.id = id;
        this.paymentNumber = paymentNumber;
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.cancelledAt = cancelledAt;
    }

    public static Payment create(Long orderId, Long memberId, int amount, PaymentMethod method) {
        validateAmount(amount);
        return Payment.builder()
                .paymentNumber(generatePaymentNumber())
                .orderId(orderId)
                .memberId(memberId)
                .amount(amount)
                .method(method)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void complete() {
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }
        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {
        if (this.status == PaymentStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_CANCELLED);
        }
        if (!this.status.isCancellable()) {
            throw new BusinessException(ErrorCode.PAYMENT_CANNOT_BE_CANCELLED);
        }
        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.PAYMENT_CANNOT_BE_CANCELLED);
        }
        this.status = PaymentStatus.REFUNDED;
        this.cancelledAt = LocalDateTime.now();
    }

    public boolean isPaid() {
        return this.status == PaymentStatus.COMPLETED;
    }

    private static void validateAmount(int amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }

    private static String generatePaymentNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
