package edts.week6_practice1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration
 * Test Trigger
 *
 * <p>This class configures the OpenAPI documentation for the REST API.
 * It provides API metadata, server information, and endpoint grouping.</p>
 *
 * <p>Access Swagger UI at: http://localhost:8080/swagger-ui/index.html</p>
 * <p>Access OpenAPI JSON at: http://localhost:8080/v3/api-docs</p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${spring.application.name:Mini E-Commerce API}")
    private String applicationName;

    /**
     * Configure OpenAPI bean with API information.
     *
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI miniECommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .description("""
                                ### Mini E-Commerce Backend Platform REST API

                                A comprehensive REST API for managing products and categories in an e-commerce system.

                                **Features:**
                                - Category Management (CRUD)
                                - Product Management (CRUD)
                                - Product Search & Filtering
                                - Pagination Support
                                - Soft Delete Implementation
                                - Centralized Error Handling
                                - Request Validation

                                **Authentication:**
                                Currently, the API does not require authentication. This will be implemented in future versions.

                                **Error Handling:**
                                All errors follow a consistent format:
                                ```json
                                {
                                  "code": "ERROR_CODE",
                                  "message": "Error description",
                                  "details": ["Detail 1", "Detail 2"]
                                }
                                ```

                                **Pagination:**
                                All list endpoints support pagination:
                                - `page`: Page number (default: 0)
                                - `size`: Page size (default: 10)
                                - `sortBy`: Field to sort by (default: name)
                                - `sortDir`: Sort direction - asc/desc (default: asc)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dafa Ardiansyah")
                                .email("ardidafa21@gmail.com")
                                .url("https://glanze.space/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.ecommerce.com")
                                .description("Production Server")
                ));
    }

    /**
     * Group Category API endpoints.
     *
     * @return GroupedOpenApi for category endpoints
     */
    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("01. Categories")
                .pathsToMatch("/api/v1/categories/**")
                .build();
    }

    /**
     * Group Product API endpoints.
     *
     * @return GroupedOpenApi for product endpoints
     */
    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("02. Products")
                .pathsToMatch("/api/v1/products/**")
                .build();
    }

}
