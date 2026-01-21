package ex.ecommerceserver.global.security.oauth2;

import java.util.Map;

public record GitHubOAuth2UserInfo(
        String providerId,
        String email,
        String name
) implements OAuth2UserInfo {

    public static GitHubOAuth2UserInfo from(Map<String, Object> attributes) {
        return new GitHubOAuth2UserInfo(
                String.valueOf(attributes.get("id")),
                (String) attributes.get("email"),
                (String) attributes.get("name")
        );
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GITHUB;
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
