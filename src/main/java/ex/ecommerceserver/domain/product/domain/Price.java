package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;

public record Price(int value) {

    public Price {
        if (value < 0) {
            throw new BusinessException(ErrorCode.INVALID_PRICE);
        }
    }

    public static Price of(int value) {
        return new Price(value);
    }
}
