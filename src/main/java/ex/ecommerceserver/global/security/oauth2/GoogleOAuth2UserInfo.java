package ex.ecommerceserver.global.security.oauth2;

import java.util.Map;

public record GoogleOAuth2UserInfo(
        String providerId,
        String email,
        String name
) implements OAuth2UserInfo {

    public static GoogleOAuth2UserInfo from(Map<String, Object> attributes) {
        return new GoogleOAuth2UserInfo(
                (String) attributes.get("sub"),
                (String) attributes.get("email"),
                (String) attributes.get("name")
        );
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}
