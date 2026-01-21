package ex.ecommerceserver.domain.member.api;

import ex.ecommerceserver.domain.member.application.MemberApplication;
import ex.ecommerceserver.domain.member.application.dto.MemberResponse;
import ex.ecommerceserver.domain.member.application.dto.MemberUpdateRequest;
import ex.ecommerceserver.domain.member.domain.Role;
import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberApiTest {

    @InjectMocks
    private MemberApi memberApi;

    @Mock
    private MemberApplication memberApplication;

    @Nested
    @DisplayName("getMyInfo")
    class GetMyInfo {

        @Test
        @DisplayName("회원 ID로 내 정보를 조회한다")
        void getMyInfo_ValidMemberId_ReturnsMyInfo() {
            // given
            Long memberId = 1L;
            MemberResponse response = new MemberResponse(
                    1L,
                    "test@gmail.com",
                    "테스터",
                    OAuth2Provider.GOOGLE,
                    Role.CUSTOMER,
                    LocalDateTime.now()
            );
            given(memberApplication.getMyInfo(memberId)).willReturn(response);

            // when
            ResponseEntity<MemberResponse> result = memberApi.getMyInfo(memberId);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().id()).isEqualTo(1L);
            assertThat(result.getBody().email()).isEqualTo("test@gmail.com");
            assertThat(result.getBody().name()).isEqualTo("테스터");
            assertThat(result.getBody().provider()).isEqualTo(OAuth2Provider.GOOGLE);
        }
    }

    @Nested
    @DisplayName("updateMyInfo")
    class UpdateMyInfo {

        @Test
        @DisplayName("회원 정보를 수정하고 수정된 정보를 반환한다")
        void updateMyInfo_ValidRequest_ReturnsUpdatedInfo() {
            // given
            Long memberId = 1L;
            MemberUpdateRequest request = new MemberUpdateRequest("새이름");
            MemberResponse response = new MemberResponse(
                    1L,
                    "test@gmail.com",
                    "새이름",
                    OAuth2Provider.GOOGLE,
                    Role.CUSTOMER,
                    LocalDateTime.now()
            );
            given(memberApplication.updateMyInfo(eq(memberId), any(MemberUpdateRequest.class)))
                    .willReturn(response);

            // when
            ResponseEntity<MemberResponse> result = memberApi.updateMyInfo(memberId, request);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().name()).isEqualTo("새이름");
        }
    }
}
