package ex.ecommerceserver.domain.payment.application;

import ex.ecommerceserver.domain.order.entity.OrderEntity;
import ex.ecommerceserver.domain.order.repository.OrderRepository;
import ex.ecommerceserver.domain.payment.application.dto.CreatePaymentRequest;
import ex.ecommerceserver.domain.payment.application.dto.PaymentResponse;
import ex.ecommerceserver.domain.payment.domain.Payment;
import ex.ecommerceserver.domain.payment.domain.PaymentStatus;
import ex.ecommerceserver.domain.payment.entity.PaymentEntity;
import ex.ecommerceserver.domain.payment.repository.PaymentRepository;
import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentApplication {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse createPayment(Long memberId, CreatePaymentRequest request) {
        OrderEntity orderEntity = orderRepository.findByIdAndMemberIdWithItems(request.orderId(), memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (paymentRepository.existsByOrderIdAndStatus(request.orderId(), PaymentStatus.COMPLETED)) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_PAID);
        }

        int amount = orderEntity.toDomain().getTotalPrice();

        Payment payment = Payment.create(
                request.orderId(),
                memberId,
                amount,
                request.method()
        );

        PaymentEntity savedEntity = paymentRepository.save(PaymentEntity.from(payment));
        return PaymentResponse.from(savedEntity.toDomain());
    }

    @Transactional
    public PaymentResponse completePayment(Long memberId, Long paymentId) {
        PaymentEntity paymentEntity = getPaymentEntity(memberId, paymentId);

        Payment payment = paymentEntity.toDomain();
        payment.complete();

        paymentEntity.complete();

        OrderEntity orderEntity = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        orderEntity.toDomain().confirm();
        orderEntity.updateStatus(ex.ecommerceserver.domain.order.domain.OrderStatus.CONFIRMED);

        return PaymentResponse.from(paymentEntity.toDomain());
    }

    @Transactional
    public PaymentResponse cancelPayment(Long memberId, Long paymentId) {
        PaymentEntity paymentEntity = getPaymentEntity(memberId, paymentId);

        Payment payment = paymentEntity.toDomain();
        payment.cancel();

        paymentEntity.cancel();
        return PaymentResponse.from(paymentEntity.toDomain());
    }

    public Page<PaymentResponse> getPayments(Long memberId, Pageable pageable) {
        return paymentRepository.findByMemberId(memberId, pageable)
                .map(entity -> PaymentResponse.from(entity.toDomain()));
    }

    public Page<PaymentResponse> getPaymentsByStatus(Long memberId, PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByMemberIdAndStatus(memberId, status, pageable)
                .map(entity -> PaymentResponse.from(entity.toDomain()));
    }

    public PaymentResponse getPayment(Long memberId, Long paymentId) {
        PaymentEntity paymentEntity = getPaymentEntity(memberId, paymentId);
        return PaymentResponse.from(paymentEntity.toDomain());
    }

    private PaymentEntity getPaymentEntity(Long memberId, Long paymentId) {
        return paymentRepository.findByIdAndMemberId(paymentId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
    }
}
