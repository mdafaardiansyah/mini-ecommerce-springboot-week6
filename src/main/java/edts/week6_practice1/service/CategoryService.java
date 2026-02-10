package edts.week6_practice1.service;

import edts.week6_practice1.dto.category.CategoryRequestDTO;
import edts.week6_practice1.dto.category.CategoryResponseDTO;
import edts.week6_practice1.entity.Category;
import edts.week6_practice1.exception.BusinessException;
import edts.week6_practice1.exception.ResourceNotFoundException;
import edts.week6_practice1.repository.CategoryRepository;
import edts.week6_practice1.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Category service.
 */
@Service
@Transactional
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * Instantiates a new Category service.
     *
     * @param categoryRepository the category repository
     * @param productRepository  the product repository
     */
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Gets all categories page.
     *
     * @param pageable the pageable
     * @return the all categories page
     */
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<CategoryResponseDTO> getAllCategoriesPage(Pageable pageable) {
        log.debug("Fetching all categories with pagination");
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::mapToResponseDTO);
    }

    /**
     * Gets all categories.
     *
     * @param pageable the pageable
     * @return the all categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        log.debug("Fetching all categories with pagination");
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    /**
     * Gets category by id.
     *
     * @param id the id
     * @return the category by id
     */
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        log.debug("Fetching category with id: {}", id);
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category", "id", id);
                });
        return mapToResponseDTO(category);
    }

    /**
     * Create category category response dto.
     *
     * @param requestDTO the request dto
     * @return the category response dto
     */
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        log.info("Creating new category with name: {}", requestDTO.name());

        categoryRepository.findByNameAndIsDeletedFalse(requestDTO.name())
                .ifPresent(existing -> {
                    log.error("Category with name {} already exists", requestDTO.name());
                    throw new BusinessException("Category with name '" + requestDTO.name() + "' already exists");
                });

        Category category = new Category();
        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", savedCategory.getId());

        return mapToResponseDTO(savedCategory);
    }

    /**
     * Update category category response dto.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the category response dto
     */
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category", "id", id);
                });

        categoryRepository.findByNameAndIsDeletedFalse(requestDTO.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    log.error("Category with name {} already exists", requestDTO.name());
                    throw new BusinessException("Category with name '" + requestDTO.name() + "' already exists");
                });

        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", updatedCategory.getId());

        return mapToResponseDTO(updatedCategory);
    }

    /**
     * Delete category.
     *
     * @param id the id
     */
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category", "id", id);
                });

        long productCount = productRepository.findByCategoryIdAndIsDeletedFalse(id, org.springframework.data.domain.PageRequest.of(0, 1))
                .getTotalElements();

        if (productCount > 0) {
            log.error("Cannot delete category with id: {}. Category has associated products", id);
            throw new BusinessException("Cannot delete category with associated products");
        }

        category.setIsDeleted(true);
        categoryRepository.save(category);

        log.info("Category deleted successfully with id: {}", id);
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
