package edts.week6_practice1.repository;

import edts.week6_practice1.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Optional<Category> findByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE name = :name AND is_deleted = false", nativeQuery = true)
    Optional<Category> findByNameAndIsDeletedFalse(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM categories WHERE id = :id AND is_deleted = false", nativeQuery = true)
    int countByIdAndIsDeletedFalse(@Param("id") Long id);
}
