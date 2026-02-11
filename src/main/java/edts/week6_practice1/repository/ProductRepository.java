package edts.week6_practice1.repository;

import edts.week6_practice1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Optional<Product> findByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId AND is_deleted = false", nativeQuery = true)
    Page<Product> findByCategoryIdAndIsDeletedFalse(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = """
        SELECT * FROM products
        WHERE is_deleted = false
        AND (:name IS NULL OR name LIKE %:name%)
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        """, nativeQuery = true)
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM products WHERE is_deleted = false", nativeQuery = true)
    Page<Product> findAllActive(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM products WHERE id = :id AND is_deleted = false", nativeQuery = true)
    int countByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM products WHERE id = :id AND stock > 0 AND is_deleted = false", nativeQuery = true)
    int countByIdWithStockGreaterThanZero(@Param("id") Long id);
}
