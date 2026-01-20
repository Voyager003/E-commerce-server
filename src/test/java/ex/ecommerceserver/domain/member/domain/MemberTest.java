package ex.ecommerceserver.domain.member.domain;

import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Nested
    @DisplayName("createOAuthMember")
    class CreateOAuthMember {

        @Test
        @DisplayName("OAuth 회원 생성 시 CUSTOMER 역할로 생성된다")
        void createOAuthMember_ValidInput_CreatesWithCustomerRole() {
            // given
            String email = "test@gmail.com";
            String name = "테스터";
            OAuth2Provider provider = OAuth2Provider.GOOGLE;
            String providerId = "google123";

            // when
            Member member = Member.createOAuthMember(email, name, provider, providerId);

            // then
            assertThat(member.getEmailValue()).isEqualTo(email);
            assertThat(member.getName()).isEqualTo(name);
            assertThat(member.getProvider()).isEqualTo(provider);
            assertThat(member.getProviderId()).isEqualTo(providerId);
            assertThat(member.getRole()).isEqualTo(Role.CUSTOMER);
        }

        @Test
        @DisplayName("다양한 OAuth 제공자로 회원을 생성할 수 있다")
        void createOAuthMember_DifferentProviders_Success() {
            // given & when
            Member googleMember = Member.createOAuthMember(
                    "google@gmail.com", "구글유저", OAuth2Provider.GOOGLE, "google123"
            );
            Member githubMember = Member.createOAuthMember(
                    "github@github.com", "깃허브유저", OAuth2Provider.GITHUB, "github456"
            );

            // then
            assertThat(googleMember.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
            assertThat(githubMember.getProvider()).isEqualTo(OAuth2Provider.GITHUB);
        }
    }

    @Nested
    @DisplayName("updateProfile")
    class UpdateProfile {

        @Test
        @DisplayName("이름을 수정할 수 있다")
        void updateProfile_ValidInput_UpdatesName() {
            // given
            Member member = Member.createOAuthMember(
                    "test@gmail.com", "원래이름", OAuth2Provider.GOOGLE, "google123"
            );
            String newName = "새이름";

            // when
            member.updateProfile(newName);

            // then
            assertThat(member.getName()).isEqualTo(newName);
        }

        @Test
        @DisplayName("이름이 null이면 기존 이름을 유지한다")
        void updateProfile_NullName_KeepsOriginalName() {
            // given
            String originalName = "원래이름";
            Member member = Member.createOAuthMember(
                    "test@gmail.com", originalName, OAuth2Provider.GOOGLE, "google123"
            );

            // when
            member.updateProfile(null);

            // then
            assertThat(member.getName()).isEqualTo(originalName);
        }

        @Test
        @DisplayName("이름이 빈 문자열이면 기존 이름을 유지한다")
        void updateProfile_BlankName_KeepsOriginalName() {
            // given
            String originalName = "원래이름";
            Member member = Member.createOAuthMember(
                    "test@gmail.com", originalName, OAuth2Provider.GOOGLE, "google123"
            );

            // when
            member.updateProfile("  ");

            // then
            assertThat(member.getName()).isEqualTo(originalName);
        }
    }

    @Nested
    @DisplayName("updateFromOAuth")
    class UpdateFromOAuth {

        @Test
        @DisplayName("OAuth 정보로 이름이 업데이트된다")
        void updateFromOAuth_ValidInput_UpdatesName() {
            // given
            Member member = Member.createOAuthMember(
                    "test@gmail.com", "원래이름", OAuth2Provider.GOOGLE, "google123"
            );
            String newName = "OAuth이름";

            // when
            member.updateFromOAuth(newName);

            // then
            assertThat(member.getName()).isEqualTo(newName);
        }
    }
}
