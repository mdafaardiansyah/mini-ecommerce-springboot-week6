package edts.week6_practice1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Cross-Origin Resource Sharing (CORS) Configuration.
 *
 * <p>This configuration controls which origins, methods, and headers are allowed
 * for cross-origin requests to the API. CORS is a security feature implemented by
 * browsers to restrict cross-origin HTTP requests.</p>
 *
 * <p>Configuration follows security best practices:
 * <ul>
 *   <li>Environment-specific origin whitelisting (no wildcard in production)</li>
 *   <li>Restricted HTTP methods (only necessary methods)</li>
 *   <li>Controlled header access (specific headers only)</li>
 *   <li>Credentials support for authenticated requests</li>
 *   <li>Preflight request caching for better performance</li>
 * </ul>
 * </p>
 *
 * <p>Profiles:
 * <ul>
 *   <li><b>Development (dev):</b> Permissive CORS for local development</li>
 *   <li><b>Production (prod):</b> Strict CORS with whitelisted origins only</li>
 * </ul>
 * </p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@Configuration
public class CorsConfig {

    /**
     * Development CORS Configuration.
     *
     * <p>Permissive settings for local development:
     * <ul>
     *   <li>Allows all localhost origins (various ports)</li>
     *   <li>Allows all common HTTP methods</li>
     *   <li>Allows all headers (for flexibility in development)</li>
     *   <li>Credentials enabled (cookies, authorization headers)</li>
     *   <li>Max age: 1 hour (caches preflight requests)</li>
     * </ul>
     * </p>
     *
     * @return CorsConfigurationSource for development profile
     */
    @Bean
    @Profile("dev")
    public CorsConfigurationSource corsConfigurationSourceDev() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins for local development
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://0.0.0.0:*",
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:4200",
                "http://localhost:8080"
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));

        // Allowed headers (all headers for development)
        configuration.setAllowedHeaders(List.of("*"));

        // Exposed headers
        configuration.setExposedHeaders(Arrays.asList(
                "ETag", "X-Total-Count", "X-Page-Count",
                "X-Rate-Limit-Remaining", "X-Rate-Limit-Reset"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Max age for preflight requests (1 hour)
        configuration.setMaxAge(3600L);

        // Apply configuration to all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Production CORS Configuration.
     *
     * <p>Strict settings for production security:
     * <ul>
     *   <li>Whitelisted origins only (no wildcards)</li>
     *   <li>Restricted HTTP methods (only necessary methods)</li>
     *   <li>Specific allowed headers (security measure)</li>
     *   <li>Credentials enabled for authenticated endpoints</li>
     *   <li>Max age: 1 hour (caches preflight requests)</li>
     * </ul>
     * </p>
     *
     * <p><b>IMPORTANT:</b> Add your production frontend domains to the allowed origins list!</p>
     *
     * @return CorsConfigurationSource for production profile
     */
    @Bean
    @Profile("prod")
    public CorsConfigurationSource corsConfigurationSourceProd() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ADD YOUR PRODUCTION FRONTEND DOMAINS HERE!
        configuration.setAllowedOrigins(List.of(
                "https://idp-week6.glanze.space",
                "https://www.idp-week6.glanze.space"
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept",
                "X-Requested-With", "X-Client-Version", "X-Device-ID", "X-Request-ID"
        ));

        // Exposed headers
        configuration.setExposedHeaders(Arrays.asList(
                "ETag", "X-Total-Count", "X-Page-Count",
                "X-Rate-Limit-Remaining", "X-Rate-Limit-Reset", "X-Request-ID"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Max age for preflight requests (1 hour)
        configuration.setMaxAge(3600L);

        // Apply configuration to all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
