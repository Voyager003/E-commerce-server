package ex.ecommerceserver.domain.product.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;

public record Stock(int quantity) {

    public Stock {
        if (quantity < 0) {
            throw new BusinessException(ErrorCode.INVALID_STOCK);
        }
    }

    public static Stock of(int quantity) {
        return new Stock(quantity);
    }

    public Stock decrease(int amount) {
        if (amount < 0) {
            throw new BusinessException(ErrorCode.INVALID_STOCK);
        }
        if (this.quantity < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        return new Stock(this.quantity - amount);
    }

    public Stock increase(int amount) {
        if (amount < 0) {
            throw new BusinessException(ErrorCode.INVALID_STOCK);
        }
        return new Stock(this.quantity + amount);
    }

    public boolean isEmpty() {
        return this.quantity == 0;
    }
}
