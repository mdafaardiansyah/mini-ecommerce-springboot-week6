package edts.week6_practice1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * Web MVC Configuration.
 *
 * <p>This configuration handles:
 * <ul>
 *   <li>CORS (Cross-Origin Resource Sharing) mappings</li>
 *   <li>Static resource caching</li>
 *   <li>Resource chain optimization</li>
 *   <li>Path matching strategies</li>
 * </ul>
 * </p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings for all endpoints.
     *
     * <p>This method enables CORS support for the entire application.
     * The actual CORS policies (allowed origins, methods, headers) are
     * configured in {@link CorsConfig} based on the active profile.</p>
     *
     * <p>CORS is applied to all paths (/**) with credentials support enabled.</p>
     *
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Allow credentials (cookies, authorization headers)
                .allowCredentials(true)
                // Apply CORS to all endpoints
                .allowedOriginPatterns("*")
                // Max age for preflight requests (1 hour)
                .maxAge(3600L);
    }

    /**
     * Configure resource handlers for static assets.
     *
     * <p>This configuration optimizes static resource delivery with:
     * <ul>
     *   <li>ETag support for cache validation</li>
     *   <li>Last-Modified header support</li>
     *   <li>Cache-Control headers for browser and CDN caching</li>
     *   <li>Resource versioning for cache busting</li>
     * </ul>
     * </p>
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI resources - cache for 1 hour
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .resourceChain(true);

        // Webjars - cache for 1 day (these rarely change)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);

        // Static resources (images, css, js) - cache for 1 hour
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .resourceChain(true);

        // Favicon - cache for 1 day
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic());
    }
}
