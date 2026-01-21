package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class Address {
    private final String zipCode;
    private final String street;
    private final String detail;
    private final String receiverName;
    private final String receiverPhone;

    private Address(String zipCode, String street, String detail, String receiverName, String receiverPhone) {
        this.zipCode = zipCode;
        this.street = street;
        this.detail = detail;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
    }

    public static Address of(String zipCode, String street, String detail,
                             String receiverName, String receiverPhone) {
        validate(zipCode, street, receiverName, receiverPhone);
        return new Address(zipCode, street, detail, receiverName, receiverPhone);
    }

    private static void validate(String zipCode, String street, String receiverName, String receiverPhone) {
        if (isBlank(zipCode) || isBlank(street) || isBlank(receiverName) || isBlank(receiverPhone)) {
            throw new BusinessException(ErrorCode.INVALID_ADDRESS);
        }
    }

    private static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public String getFullAddress() {
        return String.format("%s %s %s", zipCode, street, detail != null ? detail : "").trim();
    }
}
