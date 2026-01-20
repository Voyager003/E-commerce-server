package ex.ecommerceserver.domain.payment.repository;

import ex.ecommerceserver.domain.payment.domain.PaymentStatus;
import ex.ecommerceserver.domain.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByIdAndMemberId(Long id, Long memberId);

    Optional<PaymentEntity> findByOrderId(Long orderId);

    Optional<PaymentEntity> findByPaymentNumber(String paymentNumber);

    Page<PaymentEntity> findByMemberId(Long memberId, Pageable pageable);

    Page<PaymentEntity> findByMemberIdAndStatus(Long memberId, PaymentStatus status, Pageable pageable);

    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
