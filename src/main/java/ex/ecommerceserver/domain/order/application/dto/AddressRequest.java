package ex.ecommerceserver.domain.order.application.dto;

import ex.ecommerceserver.domain.order.domain.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "우편번호는 필수입니다")
        String zipCode,

        @NotBlank(message = "도로명 주소는 필수입니다")
        String street,

        String detail,

        @NotBlank(message = "수령인 이름은 필수입니다")
        String receiverName,

        @NotBlank(message = "수령인 연락처는 필수입니다")
        String receiverPhone
) {
    public Address toDomain() {
        return Address.of(zipCode, street, detail, receiverName, receiverPhone);
    }
}
