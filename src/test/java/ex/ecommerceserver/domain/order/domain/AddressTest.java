package ex.ecommerceserver.domain.order.domain;

import ex.ecommerceserver.global.exception.BusinessException;
import ex.ecommerceserver.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Nested
    @DisplayName("Address 생성")
    class Create {

        @Test
        @DisplayName("유효한 정보로 Address 객체를 생성할 수 있다")
        void create_ValidInfo_Success() {
            // given
            String zipCode = "12345";
            String street = "서울시 강남구 테헤란로 123";
            String detail = "101동 1001호";
            String receiverName = "홍길동";
            String receiverPhone = "010-1234-5678";

            // when
            Address address = Address.of(zipCode, street, detail, receiverName, receiverPhone);

            // then
            assertThat(address.getZipCode()).isEqualTo(zipCode);
            assertThat(address.getStreet()).isEqualTo(street);
            assertThat(address.getDetail()).isEqualTo(detail);
            assertThat(address.getReceiverName()).isEqualTo(receiverName);
            assertThat(address.getReceiverPhone()).isEqualTo(receiverPhone);
        }

        @Test
        @DisplayName("상세주소가 없어도 생성할 수 있다")
        void create_NullDetail_Success() {
            // when
            Address address = Address.of("12345", "서울시", null, "홍길동", "010-1234-5678");

            // then
            assertThat(address.getDetail()).isNull();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("우편번호가 비어있으면 예외가 발생한다")
        void create_BlankZipCode_ThrowsException(String zipCode) {
            // when & then
            assertThatThrownBy(() -> Address.of(zipCode, "서울시", "상세", "홍길동", "010-1234-5678"))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_ADDRESS);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("도로명 주소가 비어있으면 예외가 발생한다")
        void create_BlankStreet_ThrowsException(String street) {
            // when & then
            assertThatThrownBy(() -> Address.of("12345", street, "상세", "홍길동", "010-1234-5678"))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_ADDRESS);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("수령인 이름이 비어있으면 예외가 발생한다")
        void create_BlankReceiverName_ThrowsException(String receiverName) {
            // when & then
            assertThatThrownBy(() -> Address.of("12345", "서울시", "상세", receiverName, "010-1234-5678"))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_ADDRESS);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("수령인 연락처가 비어있으면 예외가 발생한다")
        void create_BlankReceiverPhone_ThrowsException(String receiverPhone) {
            // when & then
            assertThatThrownBy(() -> Address.of("12345", "서울시", "상세", "홍길동", receiverPhone))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_ADDRESS);
        }
    }

    @Nested
    @DisplayName("전체 주소 조회")
    class GetFullAddress {

        @Test
        @DisplayName("전체 주소를 올바르게 반환한다")
        void getFullAddress_WithDetail_ReturnsCorrectValue() {
            // given
            Address address = Address.of("12345", "서울시 강남구", "101동", "홍길동", "010-1234-5678");

            // when
            String fullAddress = address.getFullAddress();

            // then
            assertThat(fullAddress).isEqualTo("12345 서울시 강남구 101동");
        }

        @Test
        @DisplayName("상세주소가 없으면 우편번호와 도로명만 반환한다")
        void getFullAddress_WithoutDetail_ReturnsCorrectValue() {
            // given
            Address address = Address.of("12345", "서울시 강남구", null, "홍길동", "010-1234-5678");

            // when
            String fullAddress = address.getFullAddress();

            // then
            assertThat(fullAddress).isEqualTo("12345 서울시 강남구");
        }
    }
}
