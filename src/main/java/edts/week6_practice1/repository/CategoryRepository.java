package edts.week6_practice1.repository;

import edts.week6_practice1.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Optional<Category> findByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE name = :name AND is_deleted = false", nativeQuery = true)
    Optional<Category> findByNameAndIsDeletedFalse(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM categories WHERE id = :id AND is_deleted = false", nativeQuery = true)
    int countByIdAndIsDeletedFalse(@Param("id") Long id);

    /**
     * Find all categories that are not soft deleted (without pagination).
     * Overrides default findAll() to exclude soft-deleted records.
     *
     * @return List of active categories
     */
    @Query(value = "SELECT * FROM categories WHERE is_deleted = false ORDER BY name ASC", nativeQuery = true)
    List<Category> findAllActive();

    /**
     * Find all categories that are not soft deleted (with pagination).
     * This is the preferred method for paginated queries to avoid loading all records into memory.
     *
     * @param pageable the pagination parameters
     * @return Page of active categories
     */
    @Query(value = "SELECT * FROM categories WHERE is_deleted = false ORDER BY name ASC", nativeQuery = true)
    org.springframework.data.domain.Page<Category> findAllActive(Pageable pageable);
}
