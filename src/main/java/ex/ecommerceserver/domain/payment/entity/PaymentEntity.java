package ex.ecommerceserver.domain.payment.entity;

import ex.ecommerceserver.domain.payment.domain.Payment;
import ex.ecommerceserver.domain.payment.domain.PaymentMethod;
import ex.ecommerceserver.domain.payment.domain.PaymentStatus;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentNumber;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    private PaymentEntity(Long id, String paymentNumber, Long orderId, Long memberId,
                          int amount, PaymentMethod method, PaymentStatus status,
                          LocalDateTime paidAt, LocalDateTime cancelledAt) {
        this.id = id;
        this.paymentNumber = paymentNumber;
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paidAt = paidAt;
        this.cancelledAt = cancelledAt;
    }

    public static PaymentEntity from(Payment payment) {
        return new PaymentEntity(
                payment.getId(),
                payment.getPaymentNumber(),
                payment.getOrderId(),
                payment.getMemberId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getPaidAt(),
                payment.getCancelledAt()
        );
    }

    public Payment toDomain() {
        return Payment.builder()
                .id(this.id)
                .paymentNumber(this.paymentNumber)
                .orderId(this.orderId)
                .memberId(this.memberId)
                .amount(this.amount)
                .method(this.method)
                .status(this.status)
                .createdAt(this.getCreatedAt())
                .paidAt(this.paidAt)
                .cancelledAt(this.cancelledAt)
                .build();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}
