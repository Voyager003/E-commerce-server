package ex.ecommerceserver.domain.member.entity;

import ex.ecommerceserver.domain.member.domain.Member;
import ex.ecommerceserver.domain.member.domain.Role;
import ex.ecommerceserver.domain.member.domain.vo.Email;
import ex.ecommerceserver.global.common.BaseTimeEntity;
import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private MemberEntity(String email, String name, OAuth2Provider provider, String providerId, Role role) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    public static MemberEntity from(Member member) {
        return new MemberEntity(
                member.getEmailValue(),
                member.getName(),
                member.getProvider(),
                member.getProviderId(),
                member.getRole()
        );
    }

    public Member toDomain() {
        return Member.builder()
                .id(this.id)
                .email(new Email(this.email))
                .name(this.name)
                .provider(this.provider)
                .providerId(this.providerId)
                .role(this.role)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    public void update(Member member) {
        this.name = member.getName();
    }
}
