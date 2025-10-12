package fourpetals.com.controller.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/dashboard";
	}
	@GetMapping("/employees")
	public String employees(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/dashboard";
	}
	
	 @GetMapping("/profile")
	    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
	        if (userDetails != null) {
	            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
	            if (userOpt.isPresent()) {
	                model.addAttribute("user", userOpt.get());
	            } else {
	                model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
	                return "manager/error";
	            }
	        }
	        return "manager/profile";
	    }

}
