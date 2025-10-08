package fourpetals.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fourpetals.com.entity.User;
import fourpetals.com.repository.UserRepository;

@Controller
public class RegisterController {
	private final UserRepository userRepository;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "auth/register"; // trỏ tới register.html trong templates
    }

    // Xử lý khi submit form
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        // lưu user xuống DB bằng JPA
        userRepository.save(user);
        model.addAttribute("success", "Đăng ký thành công!");
        return "auth/register";
    }
}
