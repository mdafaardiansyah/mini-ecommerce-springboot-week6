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
 * Configuration for Security Headers.
 *
 * <p>This filter adds security-related HTTP headers to every response
 * to enhance application security and protect against common web vulnerabilities.</p>
 *
 * <p>Security headers added:
 * <ul>
 *   <li>Strict-Transport-Security (HSTS): Enforces HTTPS connections</li>
 *   <li>X-Content-Type-Options: Prevents MIME type sniffing</li>
 *   <li>X-Frame-Options: Prevents clickjacking attacks</li>
 *   <li>X-XSS-Protection: Enables XSS filtering in browsers</li>
 *   <li>Content-Security-Policy: Controls resource loading</li>
 *   <li>Referrer-Policy: Controls referrer information</li>
 * </ul>
 * </p>
 *
 * @author EDTS Team
 * @version 1.0.0
 */
@Configuration
public class SecurityHeadersConfig {

    /**
     * Creates a filter that adds security headers to all HTTP responses.
     *
     * @return OncePerRequestFilter for security headers
     */
    @Bean
    public OncePerRequestFilter securityHeadersFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

                // Strict-Transport-Security (HSTS)
                // Forces browser to use HTTPS for max-age of 1 year (31536000 seconds)
                // includeSubDomains ensures all subdomains also use HTTPS
                response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

                // X-Content-Type-Options: nosniff
                // Prevents browser from MIME-sniffing the response type
                response.setHeader("X-Content-Type-Options", "nosniff");

                // X-Frame-Options: DENY
                // Prevents page from being displayed in a frame or iframe (prevents clickjacking)
                response.setHeader("X-Frame-Options", "DENY");

                // X-XSS-Protection: 1; mode=block
                // Enables XSS protection in browsers that support it
                response.setHeader("X-XSS-Protection", "1; mode=block");

                // Content-Security-Policy
                // Controls which resources the browser is allowed to load
                // Default policy allows same-origin resources
                response.setHeader("Content-Security-Policy",
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data: https:; " +
                        "font-src 'self' data:; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none'");

                // Referrer-Policy: strict-origin-when-cross-origin
                // Controls how much referrer information is sent with requests
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

                // Permissions-Policy (formerly Feature-Policy)
                // Controls which browser features can be used
                response.setHeader("Permissions-Policy",
                        "geolocation=(), " +
                        "microphone=(), " +
                        "camera=(), " +
                        "payment=()");

                // Continue the filter chain
                filterChain.doFilter(request, response);
            }
        };
    }
}
