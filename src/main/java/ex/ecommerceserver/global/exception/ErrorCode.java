package ex.ecommerceserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 오류가 발생했습니다"),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "회원을 찾을 수 없습니다"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "M002", "이메일 형식이 올바르지 않습니다"),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다"),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다"),
    PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "P002", "현재 구매할 수 없는 상품입니다"),
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "P003", "상품명이 유효하지 않습니다"),
    DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "P004", "이미 존재하는 상품명입니다"),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "P005", "가격이 유효하지 않습니다"),
    INVALID_STOCK(HttpStatus.BAD_REQUEST, "P006", "재고 수량이 유효하지 않습니다"),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "P007", "재고가 부족합니다"),
    CANNOT_ACTIVATE_DISCONTINUED_PRODUCT(HttpStatus.BAD_REQUEST, "P008", "판매 중단된 상품은 활성화할 수 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
