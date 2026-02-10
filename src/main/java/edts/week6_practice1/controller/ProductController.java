package edts.week6_practice1.controller;

import edts.week6_practice1.dto.PagedResponseDTO;
import edts.week6_practice1.dto.product.ProductRequestDTO;
import edts.week6_practice1.dto.product.ProductResponseDTO;
import edts.week6_practice1.dto.product.ProductSearchDTO;
import edts.week6_practice1.service.ProductService;
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

import java.math.BigDecimal;

/**
 * REST Controller for Product Management.
 *
 * <p>Provides endpoints for managing products in the e-commerce system including
 * CRUD operations, search, filtering, and category-based retrieval.</p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "APIs for managing products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    /**
     * Constructor-based dependency injection.
     *
     * @param productService the product service
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieve all products with pagination and sorting.
     *
     * @param page   the page number (0-based, default: 0)
     * @param size   the page size (default: 10)
     * @param sortBy the field to sort by (default: name)
     * @param sortDir the sort direction - asc or desc (default: asc)
     * @return paginated list of products
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all products",
            description = """
                    Retrieve a paginated list of all products.
                    Supports sorting by any field and customizable page size.
                    Returns products that are not marked as deleted.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved products",
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
    public ResponseEntity<PagedResponseDTO<ProductResponseDTO>> getAllProducts(
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
                    schema = @Schema(type = "string", defaultValue = "name",
                            allowableValues = {"id", "name", "price", "stock", "createdAt", "updatedAt"})
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
        log.info("GET /api/v1/products - Fetching all products");
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponseDTO> productPage = productService.getAllProductsPage(pageable);

        PagedResponseDTO<ProductResponseDTO> response = new PagedResponseDTO<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a product by its ID.
     *
     * @param id the product ID
     * @return the product details
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get product by ID",
            description = """
                    Retrieve detailed information about a specific product by its ID.
                    Returns 404 if the product does not exist or has been deleted.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved product",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(
                    name = "id",
                    description = "Product ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id
    ) {
        log.info("GET /api/v1/products/{} - Fetching product by id", id);
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Search products with filters.
     *
     * @param name     search by product name (optional, partial match)
     * @param minPrice filter by minimum price (optional)
     * @param maxPrice filter by maximum price (optional)
     * @param page     the page number (default: 0)
     * @param size     the page size (default: 10)
     * @param sortBy   the field to sort by (default: name)
     * @param sortDir  the sort direction (default: asc)
     * @return paginated list of filtered products
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Search products",
            description = """
                    Search products with optional filters for name and price range.
                    Name search is case-insensitive and partial match.
                    Price filters are inclusive.
                    All filters are optional and can be combined.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved search results",
                    content = @Content(schema = @Schema(implementation = PagedResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<PagedResponseDTO<ProductResponseDTO>> searchProducts(
            @Parameter(
                    name = "name",
                    description = "Search by product name (partial match, case-insensitive)",
                    example = "wireless",
                    required = false
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    name = "minPrice",
                    description = "Minimum price filter (inclusive)",
                    example = "20.00",
                    required = false,
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) String minPrice,

            @Parameter(
                    name = "maxPrice",
                    description = "Maximum price filter (inclusive)",
                    example = "100.00",
                    required = false,
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) String maxPrice,

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
                    schema = @Schema(type = "string", defaultValue = "name")
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
        log.info("GET /api/v1/products/search - Searching products");

        ProductSearchDTO searchDTO = new ProductSearchDTO(
                name,
                minPrice != null ? new BigDecimal(minPrice) : null,
                maxPrice != null ? new BigDecimal(maxPrice) : null
        );

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponseDTO> productPage = productService.searchProductsPage(searchDTO, pageable);

        PagedResponseDTO<ProductResponseDTO> response = new PagedResponseDTO<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve products by category.
     *
     * @param categoryId the category ID
     * @param page      the page number (default: 0)
     * @param size      the page size (default: 10)
     * @param sortBy    the field to sort by (default: name)
     * @param sortDir   the sort direction (default: asc)
     * @return paginated list of products in the category
     */
    @GetMapping(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get products by category",
            description = """
                    Retrieve all products belonging to a specific category.
                    Returns 404 if the category does not exist.
                    Supports pagination and sorting.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = PagedResponseDTO.class))
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
    public ResponseEntity<PagedResponseDTO<ProductResponseDTO>> getProductsByCategory(
            @Parameter(
                    name = "categoryId",
                    description = "Category ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long categoryId,

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
                    schema = @Schema(type = "string", defaultValue = "name")
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
        log.info("GET /api/v1/products/category/{} - Fetching products by category", categoryId);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponseDTO> productPage = productService.getProductsByCategoryPage(categoryId, pageable);

        PagedResponseDTO<ProductResponseDTO> response = new PagedResponseDTO<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Create a new product.
     *
     * @param requestDTO the product details
     * @return the created product
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new product",
            description = """
                    Create a new product in the system.
                    Price must be >= 0 and stock must be >= 0.
                    SKU must be unique if provided.
                    Returns 400 if the category does not exist.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or category not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<ProductResponseDTO> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductRequestDTO.class))
            )
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        log.info("POST /api/v1/products - Creating new product");
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Update an existing product.
     *
     * @param id         the product ID
     * @param requestDTO the updated product details
     * @return the updated product
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update a product",
            description = """
                    Update an existing product's details.
                    Price must be >= 0 and stock must be >= 0.
                    Returns 404 if the product or category does not exist.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or category not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(
                    name = "id",
                    description = "Product ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated product details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductRequestDTO.class))
            )
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        log.info("PUT /api/v1/products/{} - Updating product", id);
        ProductResponseDTO updatedProduct = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Delete a product.
     *
     * @param id the product ID
     * @return 204 No Content on success
     */
    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a product",
            description = """
                    Delete a product by marking it as deleted (soft delete).
                    Returns 400 if the product has stock > 0.
                    Returns 404 if the product does not exist.
                    """,
            tags = {"Products"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete product with stock > 0",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = edts.week6_practice1.exception.ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(
                    name = "id",
                    description = "Product ID",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id
    ) {
        log.info("DELETE /api/v1/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
