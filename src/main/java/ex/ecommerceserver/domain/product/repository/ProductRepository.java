package ex.ecommerceserver.domain.product.repository;

import ex.ecommerceserver.domain.product.domain.Category;
import ex.ecommerceserver.domain.product.domain.ProductStatus;
import ex.ecommerceserver.domain.product.entity.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithLock(@Param("id") Long id);

    boolean existsByName(String name);

    Page<ProductEntity> findByStatus(ProductStatus status, Pageable pageable);

    Page<ProductEntity> findByCategoryAndStatus(Category category, ProductStatus status, Pageable pageable);

    Page<ProductEntity> findByCategory(Category category, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE (:category IS NULL OR p.category = :category) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<ProductEntity> findByFilters(@Param("category") Category category,
                                       @Param("status") ProductStatus status,
                                       Pageable pageable);
}
