package ex.ecommerceserver.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    public record ErrorResponse(
            String code,
            String message,
            java.util.List<FieldError> errors
    ) {
        public static ErrorResponse of(ErrorCode errorCode) {
            return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), java.util.List.of());
        }

        public static ErrorResponse of(ErrorCode errorCode, org.springframework.validation.BindingResult bindingResult) {
            return new ErrorResponse(
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    bindingResult.getFieldErrors().stream()
                            .map(error -> new FieldError(
                                    error.getField(),
                                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                                    error.getDefaultMessage()))
                            .toList()
            );
        }

        public record FieldError(String field, String value, String reason) {}
    }
}
