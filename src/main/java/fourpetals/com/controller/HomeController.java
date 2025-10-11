package fourpetals.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.entity.User;
import fourpetals.com.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired
    private UserRepository userRepository;

	@GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "4Petals Flower Shop");
        return "index";
    }

	@GetMapping("/home")
	public String homePage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            // Lấy user từ DB
            User user = userRepository.findByUsername(username).orElse(null);

            // Truyền cả username và user (để header có avatar)
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

    // Hàm tiện ích dùng chung để truyền user vào model
    private void addUserToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
    }
}
