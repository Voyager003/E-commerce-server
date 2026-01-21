package ex.ecommerceserver.domain.member.domain;

import ex.ecommerceserver.domain.member.domain.vo.Email;
import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Member {

    private Long id;
    private Email email;
    private String name;
    private OAuth2Provider provider;
    private String providerId;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Member(Long id, Email email, String name, OAuth2Provider provider,
                   String providerId, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member createOAuthMember(String email, String name,
                                           OAuth2Provider provider, String providerId) {
        return Member.builder()
                .email(new Email(email))
                .name(name)
                .provider(provider)
                .providerId(providerId)
                .role(Role.CUSTOMER)
                .build();
    }

    public void updateProfile(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public void updateFromOAuth(String name) {
        this.name = name;
    }

    public String getEmailValue() {
        return email.value();
    }
}
