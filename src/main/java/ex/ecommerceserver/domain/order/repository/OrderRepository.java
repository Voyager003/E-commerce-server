package ex.ecommerceserver.domain.order.repository;

import ex.ecommerceserver.domain.order.domain.OrderStatus;
import ex.ecommerceserver.domain.order.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.id = :id AND o.memberId = :memberId")
    Optional<OrderEntity> findByIdAndMemberIdWithItems(@Param("id") Long id, @Param("memberId") Long memberId);

    Page<OrderEntity> findByMemberId(Long memberId, Pageable pageable);

    Page<OrderEntity> findByMemberIdAndStatus(Long memberId, OrderStatus status, Pageable pageable);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
