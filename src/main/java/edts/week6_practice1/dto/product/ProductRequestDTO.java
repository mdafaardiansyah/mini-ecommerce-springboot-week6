package edts.week6_practice1.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Product creation and update requests.
 *
 * @param name        The name of the product (required, max 200 characters)
 * @param description The description of the product (optional, max 1000 characters)
 * @param price       The price of the product (required, must be >= 0)
 * @param stock       The available stock quantity (required, must be >= 0)
 * @param sku         The Stock Keeping Unit (optional, max 50 characters, must be unique)
 * @param categoryId  The ID of the category this product belongs to (required)
 */
@Schema(name = "ProductRequest",
        description = "Request payload for creating or updating a product")
public record ProductRequestDTO(

        @Schema(
                name = "name",
                description = "The name of the product",
                example = "Wireless Bluetooth Headphones",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1,
                maxLength = 200
        )
        @NotBlank(message = "Product name is required")
        @Size(max = 200, message = "Product name must not exceed 200 characters")
        String name,

        @Schema(
                name = "description",
                description = "A detailed description of the product including features, specifications, and usage",
                example = "High-quality wireless headphones with active noise cancellation, 30-hour battery life, and premium sound quality",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                maxLength = 1000
        )
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Schema(
                name = "price",
                description = "The price of the product in the system's currency",
                example = "89.99",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "0"
        )
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
        BigDecimal price,

        @Schema(
                name = "stock",
                description = "The available quantity of the product in inventory",
                example = "50",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "0"
        )
        @NotNull(message = "Stock is required")
        Integer stock,

        @Schema(
                name = "sku",
                description = "Stock Keeping Unit - unique identifier for inventory management",
                example = "ELEC-001",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                maxLength = 50
        )
        @Size(max = 50, message = "SKU must not exceed 50 characters")
        String sku,

        @Schema(
                name = "categoryId",
                description = "The ID of the category this product belongs to",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Category ID is required")
        Long categoryId
) {
}
