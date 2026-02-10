package edts.week6_practice1.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param <T> The type of data being paginated
 *
 * @param data         The list of items for the current page
 * @param currentPage  The current page number (0-based)
 * @param pageSize     The number of items per page
 * @param totalElements The total number of elements across all pages
 * @param totalPages   The total number of pages available
 * @param isFirst      Whether this is the first page
 * @param isLast       Whether this is the last page
 * @param hasNext      Whether there is a next page
 * @param hasPrevious  Whether there is a previous page
 */
@Schema(name = "PagedResponse",
        description = "Generic wrapper for paginated responses with metadata")
public record PagedResponseDTO<T>(

        @Schema(
                name = "data",
                description = "List of items for the current page",
                example = "[{\"id\": 1, \"name\": \"Electronics\"}]"
        )
        List<T> data,

        @Schema(
                name = "currentPage",
                description = "The current page number (0-based indexing)",
                example = "0",
                minimum = "0"
        )
        int currentPage,

        @Schema(
                name = "pageSize",
                description = "The number of items per page",
                example = "10",
                minimum = "1"
        )
        int pageSize,

        @Schema(
                name = "totalElements",
                description = "Total number of elements across all pages",
                example = "100"
        )
        long totalElements,

        @Schema(
                name = "totalPages",
                description = "Total number of pages available",
                example = "10",
                minimum = "0"
        )
        int totalPages,

        @Schema(
                name = "isFirst",
                description = "Indicates if this is the first page",
                example = "true"
        )
        boolean isFirst,

        @Schema(
                name = "isLast",
                description = "Indicates if this is the last page",
                example = "false"
        )
        boolean isLast,

        @Schema(
                name = "hasNext",
                description = "Indicates if there is a next page",
                example = "true"
        )
        boolean hasNext,

        @Schema(
                name = "hasPrevious",
                description = "Indicates if there is a previous page",
                example = "false"
        )
        boolean hasPrevious
) {
    /**
     * Simplified constructor that automatically calculates navigation flags.
     *
     * @param data         The list of items
     * @param currentPage  The current page number
     * @param pageSize     The page size
     * @param totalElements Total elements
     * @param totalPages   Total pages
     */
    public PagedResponseDTO(List<T> data, int currentPage, int pageSize, long totalElements, int totalPages) {
        this(data, currentPage, pageSize, totalElements, totalPages,
                currentPage == 0,
                currentPage >= totalPages - 1 || totalPages == 0,
                currentPage < totalPages - 1,
                currentPage > 0
        );
    }
}
