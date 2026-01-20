package ex.ecommerceserver.domain.cart.repository;

import ex.ecommerceserver.domain.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByMemberId(Long memberId);

    @Query("SELECT c FROM CartEntity c LEFT JOIN FETCH c.items WHERE c.memberId = :memberId")
    Optional<CartEntity> findByMemberIdWithItems(@Param("memberId") Long memberId);
}
