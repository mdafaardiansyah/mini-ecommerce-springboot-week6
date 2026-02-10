package edts.week6_practice1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root Controller for handling the base URL path.
 *
 * <p>This controller redirects the root path "/" to Swagger UI documentation,
 * providing a better developer experience when accessing the application's base URL.</p>
 *
 * @author EDTS
 * @version 1.0
 */
@Controller
public class RootController {

    /**
     * Redirects the root URL to Swagger UI.
     *
     * <p>When users access the base URL (e.g., https://app.herokuapp.com/),
     * they will be automatically redirected to the Swagger UI documentation
     * at /swagger-ui/index.html where they can explore and test all available APIs.</p>
     *
     * @return String redirect instruction to Swagger UI
     */
    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
