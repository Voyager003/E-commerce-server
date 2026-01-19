package ex.ecommerceserver.global.security.oauth2;

import ex.ecommerceserver.domain.member.domain.Member;
import ex.ecommerceserver.domain.member.entity.MemberEntity;
import ex.ecommerceserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create(registrationId, oAuth2User.getAttributes());

        MemberEntity memberEntity = memberRepository
                .findByProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId())
                .map(entity -> {
                    Member member = entity.toDomain();
                    member.updateFromOAuth(userInfo.getName());
                    entity.update(member);
                    return entity;
                })
                .orElseGet(() -> {
                    Member newMember = Member.createOAuthMember(
                            userInfo.getEmail(),
                            userInfo.getName(),
                            userInfo.getProvider(),
                            userInfo.getProviderId()
                    );
                    return memberRepository.save(MemberEntity.from(newMember));
                });

        Member member = memberEntity.toDomain();

        return new CustomOAuth2User(
                member.getId(),
                member.getEmailValue(),
                member.getRole(),
                oAuth2User.getAttributes()
        );
    }
}
