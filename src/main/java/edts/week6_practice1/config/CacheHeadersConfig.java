package edts.week6_practice1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Configuration for HTTP Caching Headers.
 *
 * <p>This filter adds cache-related HTTP headers to responses to optimize
 * performance and reduce server load by allowing clients and CDNs (like Cloudflare)
 * to cache appropriate responses.</p>
 *
 * <p>Caching strategy:
 * <ul>
 *   <li>GET requests for API endpoints: Cache for 5 minutes (short-term)</li>
 *   <li>Static resources: Cache for 1 hour (long-term)</li>
 *   <li>POST/PUT/DELETE: No caching (must-revalidate)</li>
 * </ul>
 * </p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@Configuration
public class CacheHeadersConfig {

    private static final String API_ENDPOINTS_PATTERN = "/api/v1/*";

    /**
     * Creates a filter that adds cache headers to HTTP responses.
     *
     * <p>Cache headers are added based on the request method and URI:
     * <ul>
     *   <li>GET requests: Add Cache-Control for client-side caching</li>
     *   <li>POST/PUT/DELETE: Set no-cache to prevent stale data</li>
     * </ul>
     * </p>
     *
     * @return OncePerRequestFilter for cache headers
     */
    @Bean
    public OncePerRequestFilter cacheHeadersFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

                String method = request.getMethod();
                String uri = request.getRequestURI();

                // Add ETag header for cache validation (ETag support)
                // This enables conditional requests (If-None-Match)
                response.setHeader("ETag", "W/\"" + System.currentTimeMillis() + "\"");

                // Set cache headers based on request method and URI
                if ("GET".equalsIgnoreCase(method)) {
                    if (uri.startsWith("/api/v1/")) {
                        // API endpoints: Short-term caching (5 minutes)
                        // Allows Cloudflare/CDN to cache, but with short duration
                        response.setHeader("Cache-Control", "public, max-age=300, s-maxage=300, must-revalidate");
                        response.setHeader("Vary", "Accept, Accept-Encoding, Accept-Language");
                    } else if (uri.startsWith("/swagger-ui") || uri.startsWith("/webjars")) {
                        // Swagger UI and static resources: Long-term caching (1 hour)
                        response.setHeader("Cache-Control", "public, max-age=3600, immutable");
                    } else if (uri.startsWith("/actuator")) {
                        // Actuator endpoints: No caching (real-time metrics)
                        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                        response.setHeader("Pragma", "no-cache");
                        response.setHeader("Expires", "0");
                    }
                } else if ("POST".equalsIgnoreCase(method) ||
                          "PUT".equalsIgnoreCase(method) ||
                          "DELETE".equalsIgnoreCase(method)) {
                    // Write operations: Never cache
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Expires", "0");
                }

                // Continue the filter chain
                filterChain.doFilter(request, response);
            }
        };
    }
}
