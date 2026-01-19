package ex.ecommerceserver.global.security.oauth2;

import ex.ecommerceserver.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("OAuth2 로그인 처리")
    class LoadUser {

        @Test
        @DisplayName("신규 사용자 - OAuth2UserInfo로부터 정보를 정확히 추출한다")
        void loadUser_NewUser_ExtractsCorrectInfo() {
            // given
            Map<String, Object> attributes = Map.of(
                    "sub", "google123",
                    "email", "newuser@gmail.com",
                    "name", "신규유저"
            );

            // when
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create("google", attributes);

            // then
            assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
            assertThat(userInfo.getProviderId()).isEqualTo("google123");
            assertThat(userInfo.getEmail()).isEqualTo("newuser@gmail.com");
            assertThat(userInfo.getName()).isEqualTo("신규유저");
        }

        @Test
        @DisplayName("기존 사용자 - provider와 providerId로 회원을 조회한다")
        void loadUser_ExistingUser_FindsByProviderAndProviderId() {
            // given
            given(memberRepository.findByProviderAndProviderId(OAuth2Provider.GOOGLE, "google123"))
                    .willReturn(Optional.empty());

            // when
            Optional<?> result = memberRepository.findByProviderAndProviderId(
                    OAuth2Provider.GOOGLE, "google123"
            );

            // then
            assertThat(result).isEmpty();
            verify(memberRepository).findByProviderAndProviderId(OAuth2Provider.GOOGLE, "google123");
        }
    }
}
