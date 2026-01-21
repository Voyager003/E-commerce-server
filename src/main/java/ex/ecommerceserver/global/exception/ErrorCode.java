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
    CANNOT_ACTIVATE_DISCONTINUED_PRODUCT(HttpStatus.BAD_REQUEST, "P008", "판매 중단된 상품은 활성화할 수 없습니다"),

    // Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CA001", "장바구니를 찾을 수 없습니다"),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CA002", "장바구니 항목을 찾을 수 없습니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "CA003", "유효하지 않은 수량입니다"),
    CART_ITEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "CA004", "이미 장바구니에 있는 상품입니다"),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "주문을 찾을 수 없습니다"),
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "O002", "주문 항목을 찾을 수 없습니다"),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O003", "유효하지 않은 주문 상태입니다"),
    ORDER_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "O004", "취소할 수 없는 주문입니다"),
    EMPTY_ORDER_ITEMS(HttpStatus.BAD_REQUEST, "O005", "주문 항목이 비어있습니다"),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "O006", "유효하지 않은 배송지 정보입니다"),

    // Payment
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PM001", "결제 정보를 찾을 수 없습니다"),
    PAYMENT_ALREADY_COMPLETED(HttpStatus.CONFLICT, "PM002", "이미 완료된 결제입니다"),
    PAYMENT_ALREADY_CANCELLED(HttpStatus.CONFLICT, "PM003", "이미 취소된 결제입니다"),
    PAYMENT_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "PM004", "취소할 수 없는 결제입니다"),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PM005", "결제 금액이 유효하지 않습니다"),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "PM006", "결제에 실패했습니다"),
    ORDER_ALREADY_PAID(HttpStatus.CONFLICT, "PM007", "이미 결제된 주문입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
