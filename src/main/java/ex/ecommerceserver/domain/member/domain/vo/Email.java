package ex.ecommerceserver.domain.member.domain.vo;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public Email {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT, "이메일은 필수입니다");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }
}
