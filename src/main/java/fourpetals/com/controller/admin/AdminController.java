package fourpetals.com.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	 @GetMapping("/dashboard")
	    public String dashboard(Model model) {
	        model.addAttribute("totalUsers", 100);
	        model.addAttribute("todayOrders", 25);
	        model.addAttribute("revenue", 5000000);
	        
	        return "admin/dashboard";
	    }

	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("pageTitle", "Admin Profile");
		model.addAttribute("contentFragment", "admin/profile :: profileContent");
		return "admin/profile";
	}
}