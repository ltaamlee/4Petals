package fourpetals.com.controller.manager.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerUserController {
	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("pageTitle", "Quản lý User");
		return "manager/users/list";
	}
}
