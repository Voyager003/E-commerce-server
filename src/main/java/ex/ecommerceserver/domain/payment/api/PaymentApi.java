package ex.ecommerceserver.domain.payment.api;

import ex.ecommerceserver.domain.payment.application.PaymentApplication;
import ex.ecommerceserver.domain.payment.application.dto.CreatePaymentRequest;
import ex.ecommerceserver.domain.payment.application.dto.PaymentResponse;
import ex.ecommerceserver.domain.payment.domain.PaymentStatus;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentApi {

    private final PaymentApplication paymentApplication;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @LoginMember Long memberId,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        PaymentResponse response = paymentApplication.createPayment(memberId, request);
        return ResponseEntity.created(URI.create("/api/v1/payments/" + response.id()))
                .body(response);
    }

    @PostMapping("/{paymentId}/complete")
    public ResponseEntity<PaymentResponse> completePayment(
            @LoginMember Long memberId,
            @PathVariable Long paymentId
    ) {
        PaymentResponse response = paymentApplication.completePayment(memberId, paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(
            @LoginMember Long memberId,
            @PathVariable Long paymentId
    ) {
        PaymentResponse response = paymentApplication.cancelPayment(memberId, paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getPayments(
            @LoginMember Long memberId,
            @RequestParam(required = false) PaymentStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PaymentResponse> response;
        if (status != null) {
            response = paymentApplication.getPaymentsByStatus(memberId, status, pageable);
        } else {
            response = paymentApplication.getPayments(memberId, pageable);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(
            @LoginMember Long memberId,
            @PathVariable Long paymentId
    ) {
        PaymentResponse response = paymentApplication.getPayment(memberId, paymentId);
        return ResponseEntity.ok(response);
    }
}
