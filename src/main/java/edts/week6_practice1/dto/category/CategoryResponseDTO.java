package edts.week6_practice1.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Category response.
 *
 * @param id          The unique identifier of the category
 * @param name        The name of the category
 * @param description The description of the category
 * @param createdAt   The timestamp when the category was created
 * @param updatedAt   The timestamp when the category was last updated
 */
@Schema(name = "CategoryResponse",
        description = "Response payload containing category information")
public record CategoryResponseDTO(

        @Schema(
                name = "id",
                description = "Unique identifier of the category",
                example = "1"
        )
        Long id,

        @Schema(
                name = "name",
                description = "The name of the category",
                example = "Electronics"
        )
        String name,

        @Schema(
                name = "description",
                description = "A detailed description of the category",
                example = "Electronic devices and accessories"
        )
        String description,

        @Schema(
                name = "createdAt",
                description = "Timestamp when the category was created",
                example = "2026-02-09T10:00:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime createdAt,

        @Schema(
                name = "updatedAt",
                description = "Timestamp when the category was last updated",
                example = "2026-02-09T10:00:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}
