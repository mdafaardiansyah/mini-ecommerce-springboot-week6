package edts.week6_practice1.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Standard error response format for all API errors.
 *
 * <p>This format is used consistently across all endpoints for error handling.</p>
 *
 * @param code    The error code identifying the type of error
 * @param message A human-readable error message
 * @param details Additional error details (e.g., validation errors)
 */
@Schema(name = "ErrorResponse",
        description = "Standard error response format for API errors")
public record ErrorResponse(

        @Schema(
                name = "code",
                description = "Error code identifying the type of error",
                example = "RESOURCE_NOT_FOUND",
                allowableValues = {"RESOURCE_NOT_FOUND", "BUSINESS_ERROR", "VALIDATION_ERROR", "INTERNAL_SERVER_ERROR"}
        )
        String code,

        @Schema(
                name = "message",
                description = "Human-readable error message describing what went wrong",
                example = "Product not found with id: '999'"
        )
        String message,

        @Schema(
                name = "details",
                description = "Additional error details, typically used for validation errors",
                example = "[\"name: Category name is required\", \"price: Price must be greater than 0\"]"
        )
        List<String> details
) {
    /**
     * Simplified constructor for errors without details.
     *
     * @param code    The error code
     * @param message The error message
     */
    public ErrorResponse(String code, String message) {
        this(code, message, List.of());
    }
}
