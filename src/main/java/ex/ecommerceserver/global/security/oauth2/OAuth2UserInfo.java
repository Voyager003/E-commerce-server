package ex.ecommerceserver.global.security.oauth2;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
