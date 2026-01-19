package ex.ecommerceserver.domain.member.domain.vo;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Nested
    @DisplayName("Email 생성")
    class Create {

        @Test
        @DisplayName("유효한 이메일로 Email 객체를 생성할 수 있다")
        void create_ValidEmail_Success() {
            // given
            String validEmail = "test@gmail.com";

            // when
            Email email = new Email(validEmail);

            // then
            assertThat(email.value()).isEqualTo(validEmail);
        }

        @ParameterizedTest
        @ValueSource(strings = {"user@example.com", "user.name@example.co.kr", "user+tag@example.com"})
        @DisplayName("다양한 유효한 이메일 형식으로 생성할 수 있다")
        void create_VariousValidEmails_Success(String validEmail) {
            // when
            Email email = new Email(validEmail);

            // then
            assertThat(email.value()).isEqualTo(validEmail);
        }

        @Test
        @DisplayName("null 이메일로 생성하면 예외가 발생한다")
        void create_NullEmail_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> new Email(null))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        @Test
        @DisplayName("빈 이메일로 생성하면 예외가 발생한다")
        void create_BlankEmail_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> new Email("   "))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "invalid@", "@invalid.com", "invalid@.com", "invalid@com"})
        @DisplayName("유효하지 않은 이메일 형식으로 생성하면 예외가 발생한다")
        void create_InvalidEmailFormat_ThrowsException(String invalidEmail) {
            // when & then
            assertThatThrownBy(() -> new Email(invalidEmail))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }
}
