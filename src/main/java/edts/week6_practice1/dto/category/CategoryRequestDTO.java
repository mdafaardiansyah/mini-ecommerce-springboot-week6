package edts.week6_practice1.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Category creation and update requests.
 *
 * @param name        The name of the category (required, max 100 characters)
 * @param description The description of the category (optional, max 500 characters)
 */
@Schema(name = "CategoryRequest",
        description = "Request payload for creating or updating a category")
public record CategoryRequestDTO(

        @Schema(
                name = "name",
                description = "The name of the category",
                example = "Electronics",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1,
                maxLength = 100
        )
        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        String name,

        @Schema(
                name = "description",
                description = "A detailed description of the category",
                example = "Electronic devices and accessories including smartphones, laptops, and gadgets",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                maxLength = 500
        )
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) {
}
