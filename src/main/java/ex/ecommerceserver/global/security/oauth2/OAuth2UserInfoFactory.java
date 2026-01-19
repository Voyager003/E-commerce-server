package ex.ecommerceserver.global.security.oauth2;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo create(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> GoogleOAuth2UserInfo.from(attributes);
            // 추후 다른 OAuth2 제공자 추가
            // case "kakao" -> KakaoOAuth2UserInfo.from(attributes);
            // case "naver" -> NaverOAuth2UserInfo.from(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + registrationId);
        };
    }
}
