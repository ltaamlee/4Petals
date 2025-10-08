package fourpetals.com.controller.admin.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.User;
import fourpetals.com.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String listUsers(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "admin/users/list";
	}

}
