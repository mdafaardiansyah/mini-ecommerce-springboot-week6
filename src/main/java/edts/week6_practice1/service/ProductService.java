package edts.week6_practice1.service;

import edts.week6_practice1.dto.category.CategoryResponseDTO;
import edts.week6_practice1.dto.product.ProductRequestDTO;
import edts.week6_practice1.dto.product.ProductResponseDTO;
import edts.week6_practice1.dto.product.ProductSearchDTO;
import edts.week6_practice1.entity.Category;
import edts.week6_practice1.entity.Product;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProductsPage(Pageable pageable) {
        log.debug("Fetching all products with pagination");
        Page<Product> products = productRepository.findAllActive(pageable);
        return products.map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        log.debug("Fetching product with id: {}", id);
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product", "id", id);
                });
        return mapToResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsPage(ProductSearchDTO searchDTO, Pageable pageable) {
        log.debug("Searching products with criteria: name={}, minPrice={}, maxPrice={}",
                searchDTO.name(), searchDTO.minPrice(), searchDTO.maxPrice());

        Page<Product> products = productRepository.searchProducts(
                searchDTO.name() != null ? searchDTO.name() : null,
                searchDTO.minPrice(),
                searchDTO.maxPrice(),
                pageable
        );

        log.info("Found {} products matching search criteria", products.getTotalElements());
        return products.map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategoryPage(Long categoryId, Pageable pageable) {
        log.debug("Fetching products for category id: {}", categoryId);

        if (categoryRepository.countByIdAndIsDeletedFalse(categoryId) == 0) {
            log.error("Category not found with id: {}", categoryId);
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        Page<Product> products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        return products.map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products with pagination");
        Page<Product> products = productRepository.findAllActive(pageable);
        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> searchProducts(ProductSearchDTO searchDTO, Pageable pageable) {
        log.debug("Searching products with criteria: name={}, minPrice={}, maxPrice={}",
                searchDTO.name(), searchDTO.minPrice(), searchDTO.maxPrice());

        Page<Product> products = productRepository.searchProducts(
                searchDTO.name() != null ? searchDTO.name() : null,
                searchDTO.minPrice(),
                searchDTO.maxPrice(),
                pageable
        );

        log.info("Found {} products matching search criteria", products.getTotalElements());
        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        log.debug("Fetching products for category id: {}", categoryId);

        if (categoryRepository.countByIdAndIsDeletedFalse(categoryId) == 0) {
            log.error("Category not found with id: {}", categoryId);
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        Page<Product> products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        return products.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        log.info("Creating new product with name: {}", requestDTO.name());

        if (requestDTO.price().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid price: {}", requestDTO.price());
            throw new BusinessException("Price cannot be negative");
        }

        if (requestDTO.stock() < 0) {
            log.error("Invalid stock: {}", requestDTO.stock());
            throw new BusinessException("Stock cannot be negative");
        }

        Category category = categoryRepository.findByIdAndIsDeletedFalse(requestDTO.categoryId())
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", requestDTO.categoryId());
                    return new ResourceNotFoundException("Category", "id", requestDTO.categoryId());
                });

        Product product = new Product();
        product.setName(requestDTO.name());
        product.setDescription(requestDTO.description());
        product.setPrice(requestDTO.price());
        product.setStock(requestDTO.stock());
        product.setSku(requestDTO.sku());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());

        return mapToResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product", "id", id);
                });

        if (requestDTO.price().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid price: {}", requestDTO.price());
            throw new BusinessException("Price cannot be negative");
        }

        if (requestDTO.stock() < 0) {
            log.error("Invalid stock: {}", requestDTO.stock());
            throw new BusinessException("Stock cannot be negative");
        }

        Category category = categoryRepository.findByIdAndIsDeletedFalse(requestDTO.categoryId())
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", requestDTO.categoryId());
                    return new ResourceNotFoundException("Category", "id", requestDTO.categoryId());
                });

        product.setName(requestDTO.name());
        product.setDescription(requestDTO.description());
        product.setPrice(requestDTO.price());
        product.setStock(requestDTO.stock());
        product.setSku(requestDTO.sku());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());

        return mapToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ResourceNotFoundException("Product", "id", id);
                });

        if (product.getStock() > 0) {
            log.error("Cannot delete product with id: {}. Product has stock > 0", id);
            throw new BusinessException("Produk dengan stok lebih dari 0 tidak boleh dihapus");
        }

        product.setIsDeleted(true);
        productRepository.save(product);

        log.info("Product deleted successfully with id: {}", id);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription(),
                product.getCategory().getCreatedAt(),
                product.getCategory().getUpdatedAt()
        );

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getSku(),
                categoryDTO,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
