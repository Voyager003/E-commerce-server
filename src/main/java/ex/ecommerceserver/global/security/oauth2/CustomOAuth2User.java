package ex.ecommerceserver.global.security.oauth2;

import ex.ecommerceserver.domain.member.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Long memberId;
    private final String email;
    private final Role role;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Long memberId, String email, Role role, Map<String, Object> attributes) {
        this.memberId = memberId;
        this.email = email;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getName() {
        return String.valueOf(memberId);
    }
}
