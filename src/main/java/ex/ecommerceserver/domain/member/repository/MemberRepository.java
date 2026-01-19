package ex.ecommerceserver.domain.member.repository;

import ex.ecommerceserver.domain.member.entity.MemberEntity;
import ex.ecommerceserver.global.security.oauth2.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

    boolean existsByEmail(String email);
}
