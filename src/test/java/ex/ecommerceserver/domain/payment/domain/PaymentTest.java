package ex.ecommerceserver.domain.payment.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @Nested
    @DisplayName("Payment 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 Payment 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            Long orderId = 1L;
            Long memberId = 1L;
            int amount = 50000;
            PaymentMethod method = PaymentMethod.CREDIT_CARD;

            // when
            Payment payment = Payment.create(orderId, memberId, amount, method);

            // then
            assertThat(payment.getOrderId()).isEqualTo(orderId);
            assertThat(payment.getMemberId()).isEqualTo(memberId);
            assertThat(payment.getAmount()).isEqualTo(amount);
            assertThat(payment.getMethod()).isEqualTo(method);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
            assertThat(payment.getPaymentNumber()).startsWith("PAY-");
            assertThat(payment.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("결제 금액이 0이면 예외가 발생한다")
        void create_ZeroAmount_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Payment.create(1L, 1L, 0, PaymentMethod.CREDIT_CARD))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        @Test
        @DisplayName("결제 금액이 음수이면 예외가 발생한다")
        void create_NegativeAmount_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> Payment.create(1L, 1L, -1000, PaymentMethod.CREDIT_CARD))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }

    @Nested
    @DisplayName("결제 완료")
    class Complete {

        @Test
        @DisplayName("PENDING 상태에서 결제를 완료할 수 있다")
        void complete_PendingPayment_Success() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);

            // when
            payment.complete();

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
            assertThat(payment.getPaidAt()).isNotNull();
        }

        @Test
        @DisplayName("이미 완료된 결제를 다시 완료하면 예외가 발생한다")
        void complete_CompletedPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();

            // when & then
            assertThatThrownBy(payment::complete)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }
    }

    @Nested
    @DisplayName("결제 실패")
    class Fail {

        @Test
        @DisplayName("PENDING 상태에서 결제를 실패 처리할 수 있다")
        void fail_PendingPayment_Success() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);

            // when
            payment.fail();

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        }

        @Test
        @DisplayName("이미 완료된 결제를 실패 처리하면 예외가 발생한다")
        void fail_CompletedPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();

            // when & then
            assertThatThrownBy(payment::fail)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }
    }

    @Nested
    @DisplayName("결제 취소")
    class Cancel {

        @Test
        @DisplayName("COMPLETED 상태에서 결제를 취소할 수 있다")
        void cancel_CompletedPayment_Success() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();

            // when
            payment.cancel();

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
            assertThat(payment.getCancelledAt()).isNotNull();
        }

        @Test
        @DisplayName("PENDING 상태에서는 결제를 취소할 수 없다")
        void cancel_PendingPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);

            // when & then
            assertThatThrownBy(payment::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_CANNOT_BE_CANCELLED);
        }

        @Test
        @DisplayName("이미 취소된 결제를 다시 취소하면 예외가 발생한다")
        void cancel_CancelledPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();
            payment.cancel();

            // when & then
            assertThatThrownBy(payment::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_ALREADY_CANCELLED);
        }

        @Test
        @DisplayName("FAILED 상태에서는 결제를 취소할 수 없다")
        void cancel_FailedPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.fail();

            // when & then
            assertThatThrownBy(payment::cancel)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_CANNOT_BE_CANCELLED);
        }
    }

    @Nested
    @DisplayName("환불")
    class Refund {

        @Test
        @DisplayName("COMPLETED 상태에서 환불할 수 있다")
        void refund_CompletedPayment_Success() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();

            // when
            payment.refund();

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
            assertThat(payment.getCancelledAt()).isNotNull();
        }

        @Test
        @DisplayName("PENDING 상태에서는 환불할 수 없다")
        void refund_PendingPayment_ThrowsException() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);

            // when & then
            assertThatThrownBy(payment::refund)
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PAYMENT_CANNOT_BE_CANCELLED);
        }
    }

    @Nested
    @DisplayName("결제 완료 여부 확인")
    class IsPaid {

        @Test
        @DisplayName("COMPLETED 상태면 true를 반환한다")
        void isPaid_CompletedPayment_ReturnsTrue() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();

            // when & then
            assertThat(payment.isPaid()).isTrue();
        }

        @Test
        @DisplayName("PENDING 상태면 false를 반환한다")
        void isPaid_PendingPayment_ReturnsFalse() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);

            // when & then
            assertThat(payment.isPaid()).isFalse();
        }

        @Test
        @DisplayName("CANCELLED 상태면 false를 반환한다")
        void isPaid_CancelledPayment_ReturnsFalse() {
            // given
            Payment payment = Payment.create(1L, 1L, 50000, PaymentMethod.CREDIT_CARD);
            payment.complete();
            payment.cancel();

            // when & then
            assertThat(payment.isPaid()).isFalse();
        }
    }
}
