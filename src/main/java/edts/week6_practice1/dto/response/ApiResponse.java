package edts.week6_practice1.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Standard API Response Wrapper.
 *
 * <p>This class provides a consistent response structure for all API endpoints.
 * It wraps the actual data with metadata including status code, message, and timestamp.</p>
 *
 * <p>Example response:
 * <pre>
 * {
 *   "code": 200,
 *   "status": "OK",
 *   "message": "Success",
 *   "data": { ... },
 *   "timestamp": "2026-02-10T12:00:00Z"
 * }
 * </pre>
 * </p>
 *
 * @param <T> The type of data being returned
 * @author EDTS Team
 * @version 1.0.0
 */
@Schema(description = "Standard API Response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private final int code;

    @Schema(description = "Status description", example = "OK")
    private final String status;

    @Schema(description = "Human-readable message", example = "Operation completed successfully")
    private final String message;

    @Schema(description = "Response payload")
    private final T data;

    @Schema(description = "Response timestamp in UTC (ISO 8601)", example = "2026-02-10T12:00:00Z")
    private final String timestamp;

    /**
     * Private constructor to enforce use of builder pattern.
     *
     * @param code HTTP status code
     * @param status Status description
     * @param message Human-readable message
     * @param data Response payload
     */
    private ApiResponse(int code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Creates a successful response with data.
     *
     * @param data The response payload
     * @param <T> The type of data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                200,
                "OK",
                "Operation completed successfully",
                data
        );
    }

    /**
     * Creates a successful response with custom message and data.
     *
     * @param message Custom success message
     * @param data The response payload
     * @param <T> The type of data
     * @return ApiResponse with success status and custom message
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                200,
                "OK",
                message,
                data
        );
    }

    /**
     * Creates a response with custom status code, message, and data.
     *
     * @param code HTTP status code
     * @param status Status description
     * @param message Response message
     * @param data The response payload
     * @param <T> The type of data
     * @return ApiResponse with custom parameters
     */
    public static <T> ApiResponse<T> of(int code, String status, String message, T data) {
        return new ApiResponse<>(
                code,
                status,
                message,
                data
        );
    }

    /**
     * Creates an error response.
     *
     * @param code HTTP error code
     * @param status Error status
     * @param message Error message
     * @param <T> The type of data
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(int code, String status, String message) {
        return new ApiResponse<>(
                code,
                status,
                message,
                null
        );
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
