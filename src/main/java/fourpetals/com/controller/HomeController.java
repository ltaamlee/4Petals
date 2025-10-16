package fourpetals.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.entity.User;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.CategoryService;
import fourpetals.com.service.UserService;

@Controller
public class HomeController {
	
    private UserService userService;
	private CategoryService categoryService;
	

	public HomeController(UserService userService, CategoryService categoryService) {
		super();
		this.userService = userService;
		this.categoryService = categoryService;
	}

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
        model.addAttribute("categories", categoryService.getAllCategories());
        return "customer/product";
    }

    @GetMapping("/contact")
    public String contact(Model model, Authentication authentication) {
        addUserToModel(model, authentication);
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
