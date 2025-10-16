package fourpetals.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.service.UserService;
import fourpetals.com.service.ProductService;

@Controller
public class HomeController {
	@Autowired
    private UserService userService;
	
	@Autowired
    private ProductService productService;

	@GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "4Petals Flower Shop");
        return "index";
    }

	@GetMapping("/home")
	public String homePage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            User user = userService.findByUsername(username).orElse(null);

            model.addAttribute("username", username);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("username", null);
            model.addAttribute("user", null);
        }
        return "customer/home";
    }
    
	@GetMapping("/product")
    public String product(Model model, Authentication authentication) {
        addUserToModel(model, authentication);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "customer/product";
    }

    @GetMapping("/contact")
    public String contact(Model model, Authentication authentication) {
        addUserToModel(model, authentication);
        model.addAttribute("products", productService.getAllProducts());
        return "customer/contact";
    }

    @GetMapping("/about")
    public String about(Model model, Authentication authentication) {
        addUserToModel(model, authentication);
        return "customer/about";
    }

    private void addUserToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
    }
}
