package ex.ecommerceserver.global.security.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GoogleOAuth2UserInfoTest {

    @Nested
    @DisplayName("from")
    class From {

        @Test
        @DisplayName("Google OAuth2 속성에서 사용자 정보를 추출한다")
        void from_ValidAttributes_ExtractsUserInfo() {
            // given
            Map<String, Object> attributes = Map.of(
                    "sub", "123456789",
                    "email", "user@gmail.com",
                    "name", "홍길동",
                    "picture", "https://example.com/photo.jpg"
            );

            // when
            GoogleOAuth2UserInfo userInfo = GoogleOAuth2UserInfo.from(attributes);

            // then
            assertThat(userInfo.getProviderId()).isEqualTo("123456789");
            assertThat(userInfo.getEmail()).isEqualTo("user@gmail.com");
            assertThat(userInfo.getName()).isEqualTo("홍길동");
            assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
        }

        @Test
        @DisplayName("일부 속성이 없어도 null로 처리된다")
        void from_MissingAttributes_HandlesNull() {
            // given
            Map<String, Object> attributes = Map.of(
                    "sub", "123456789"
            );

            // when
            GoogleOAuth2UserInfo userInfo = GoogleOAuth2UserInfo.from(attributes);

            // then
            assertThat(userInfo.getProviderId()).isEqualTo("123456789");
            assertThat(userInfo.getEmail()).isNull();
            assertThat(userInfo.getName()).isNull();
        }
    }
}
