package edts.week6_practice1.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Product search criteria.
 *
 * <p>All fields are optional. If provided, they will be used to filter the search results.</p>
 *
 * @param name     Search by product name (partial match, case-insensitive)
 * @param minPrice Filter by minimum price (inclusive)
 * @param maxPrice Filter by maximum price (inclusive)
 */
@Schema(name = "ProductSearchCriteria",
        description = "Search criteria for filtering products. All fields are optional.")
public record ProductSearchDTO(

        @Schema(
                name = "name",
                description = "Search products by name (partial match, case-insensitive)",
                example = "wireless",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String name,

        @Schema(
                name = "minPrice",
                description = "Filter products with price greater than or equal to this value",
                example = "20.00",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                minimum = "0"
        )
        BigDecimal minPrice,

        @Schema(
                name = "maxPrice",
                description = "Filter products with price less than or equal to this value",
                example = "100.00",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                minimum = "0"
        )
        BigDecimal maxPrice
) {
}
