package edts.week6_practice1.controller;

import edts.week6_practice1.dto.PagedResponseDTO;
import edts.week6_practice1.dto.category.CategoryRequestDTO;
import edts.week6_practice1.dto.category.CategoryResponseDTO;
import edts.week6_practice1.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Category Management.
 *
 * <p>Provides endpoints for managing product categories in the e-commerce system.</p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "APIs for managing product categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    /**
     * Constructor-based dependency injection.
     *
     * @param categoryService the category service
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retrieve all categories with pagination and sorting.
     *
     * @param page   the page number (0-based, default: 0)
     * @param size   the page size (default: 10)
     * @param sortBy the field to sort by (default: name)
     * @param sortDir the sort direction - asc or desc (default: asc)
     * @return paginated list of categories
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all categories",
            description = """
                    Retrieve a paginated list of all categories.
                    Supports sorting by any field and customizable page size.
                    Returns categories that are not marked as deleted.
                    """,
            tags = {"Categories"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved categories",
                    content = @Content(schema = @Schema(implementation = PagedResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<PagedResponseDTO<CategoryResponseDTO>> getAllCategories(
            @Parameter(
                    name = "page",
                    description = "Page number (0-based)",
                    example = "0",
                    schema = @Schema(type = "integer", minimum = "0", defaultValue = "0")
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    name = "size",
                    description = "Number of items per page",
                    example = "10",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "10")
            )
            @RequestParam(defaultValue = "10") int size,

            @Parameter(
                    name = "sortBy",
                    description = "Field to sort by",
                    example = "name",
                    schema = @Schema(type = "string", defaultValue = "name", allowableValues = {"id", "name", "createdAt", "updatedAt"})
            )
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(
                    name = "sortDir",
                    description = "Sort direction",
                    example = "asc",
                    schema = @Schema(type = "string", defaultValue = "asc", allowableValues = {"asc", "desc"})
            )
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        log.info("GET /api/v1/categories - Fetching all categories");
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryResponseDTO> categoryPage = categoryService.getAllCategoriesPage(pageable);

        PagedResponseDTO<CategoryResponseDTO> response = new PagedResponseDTO<>(
                categoryPage.getContent(),
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a category by its ID.
     *
     * @param id the category ID
     * @return the category details
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get category by ID",
            description = """
                    Retrieve detailed information about a specific category by its ID.
                    Returns 404 if the category does not exist or has been deleted.
                    """,
            tags = {"Categories"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved category",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(
                    name = "id",
                    description = "Category ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id
    ) {
        log.info("GET /api/v1/categories/{} - Fetching category by id", id);
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Create a new category.
     *
     * @param requestDTO the category details
     * @return the created category
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new category",
            description = """
                    Create a new product category.
                    Category name must be unique across all categories.
                    Returns 400 if a category with the same name already exists.
                    """,
            tags = {"Categories"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or duplicate category name",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryRequestDTO.class))
            )
            @Valid @RequestBody CategoryRequestDTO requestDTO
    ) {
        log.info("POST /api/v1/categories - Creating new category");
        CategoryResponseDTO createdCategory = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Update an existing category.
     *
     * @param id         the category ID
     * @param requestDTO the updated category details
     * @return the updated category
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update a category",
            description = """
                    Update an existing category's details.
                    Category name must remain unique after update.
                    Returns 404 if the category does not exist.
                    """,
            tags = {"Categories"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or duplicate category name",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(
                    name = "id",
                    description = "Category ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated category details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryRequestDTO.class))
            )
            @Valid @RequestBody CategoryRequestDTO requestDTO
    ) {
        log.info("PUT /api/v1/categories/{} - Updating category", id);
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Delete a category.
     *
     * @param id the category ID
     * @return 204 No Content on success
     */
    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a category",
            description = """
                    Delete a category by marking it as deleted (soft delete).
                    Returns 400 if the category has associated products.
                    Returns 404 if the category does not exist.
                    """,
            tags = {"Categories"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete category with associated products",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(
                    name = "id",
                    description = "Category ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id
    ) {
        log.info("DELETE /api/v1/categories/{} - Deleting category", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
