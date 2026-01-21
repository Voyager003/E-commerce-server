package ex.ecommerceserver.domain.member.application.dto;

import ex.ecommerceserver.domain.member.domain.Member;
import ex.ecommerceserver.domain.member.domain.Role;
import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String name,
        OAuth2Provider provider,
        Role role,
        LocalDateTime createdAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmailValue(),
                member.getName(),
                member.getProvider(),
                member.getRole(),
                member.getCreatedAt()
        );
    }
}
