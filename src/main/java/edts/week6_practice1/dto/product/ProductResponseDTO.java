package edts.week6_practice1.dto.product;

import edts.week6_practice1.dto.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Product response.
 *
 * @param id          The unique identifier of the product
 * @param name        The name of the product
 * @param description The description of the product
 * @param price       The price of the product
 * @param stock       The available stock quantity
 * @param sku         The Stock Keeping Unit
 * @param category    The category this product belongs to
 * @param createdAt   The timestamp when the product was created
 * @param updatedAt   The timestamp when the product was last updated
 */
@Schema(name = "ProductResponse",
        description = "Response payload containing product information including category details")
public record ProductResponseDTO(

        @Schema(
                name = "id",
                description = "Unique identifier of the product",
                example = "1"
        )
        Long id,

        @Schema(
                name = "name",
                description = "The name of the product",
                example = "Wireless Bluetooth Headphones"
        )
        String name,

        @Schema(
                name = "description",
                description = "A detailed description of the product",
                example = "High-quality wireless headphones with active noise cancellation"
        )
        String description,

        @Schema(
                name = "price",
                description = "The price of the product",
                example = "89.99"
        )
        BigDecimal price,

        @Schema(
                name = "stock",
                description = "The available quantity in inventory",
                example = "50"
        )
        Integer stock,

        @Schema(
                name = "sku",
                description = "Stock Keeping Unit for inventory management",
                example = "ELEC-001"
        )
        String sku,

        @Schema(
                name = "category",
                description = "The category this product belongs to",
                implementation = CategoryResponseDTO.class
        )
        CategoryResponseDTO category,

        @Schema(
                name = "createdAt",
                description = "Timestamp when the product was created",
                example = "2026-02-09T10:00:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime createdAt,

        @Schema(
                name = "updatedAt",
                description = "Timestamp when the product was last updated",
                example = "2026-02-09T10:00:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}
