package ex.ecommerceserver.global.security.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OAuth2UserInfoFactoryTest {

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("google 제공자로 GoogleOAuth2UserInfo를 생성한다")
        void create_Google_ReturnsGoogleUserInfo() {
            // given
            String registrationId = "google";
            Map<String, Object> attributes = Map.of(
                    "sub", "google123",
                    "email", "test@gmail.com",
                    "name", "테스터"
            );

            // when
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create(registrationId, attributes);

            // then
            assertThat(userInfo).isInstanceOf(GoogleOAuth2UserInfo.class);
            assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
            assertThat(userInfo.getProviderId()).isEqualTo("google123");
            assertThat(userInfo.getEmail()).isEqualTo("test@gmail.com");
            assertThat(userInfo.getName()).isEqualTo("테스터");
        }

        @Test
        @DisplayName("대소문자 구분 없이 google 제공자를 인식한다")
        void create_GoogleUpperCase_ReturnsGoogleUserInfo() {
            // given
            String registrationId = "GOOGLE";
            Map<String, Object> attributes = Map.of(
                    "sub", "google123",
                    "email", "test@gmail.com",
                    "name", "테스터"
            );

            // when
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create(registrationId, attributes);

            // then
            assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
        }

        @Test
        @DisplayName("지원하지 않는 제공자는 예외를 발생시킨다")
        void create_UnsupportedProvider_ThrowsException() {
            // given
            String registrationId = "unsupported";
            Map<String, Object> attributes = Map.of();

            // when & then
            assertThatThrownBy(() -> OAuth2UserInfoFactory.create(registrationId, attributes))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("지원하지 않는 OAuth2 제공자");
        }
    }
}
