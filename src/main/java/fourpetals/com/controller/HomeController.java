package fourpetals.com.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "4Petals Flower Shop");
        return "index";
    }

	@GetMapping("/home")
	public String homePage(Model model, Authentication authentication) {
	    if(authentication != null && authentication.isAuthenticated() 
	       && authentication.getAuthorities() != null) {
	        model.addAttribute("username", authentication.getName());
	    } else {
	        model.addAttribute("username", null);
	    }
	    return "customer/home";
	}
    
    @GetMapping("/product")
    public String product() {
    	return "customer/product";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "customer/contact";
    }
    
    @GetMapping("/about")
    public String about() {
        return "customer/about";
    }
}
