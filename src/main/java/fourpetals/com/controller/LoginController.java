package fourpetals.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fourpetals.com.entity.User;
import fourpetals.com.repository.UserRepository;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLogin() {
        return "auth/login"; // trỏ đến login.html trong thư mục templates/auth/
    }

    // Xử lý form đăng nhập
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {

        // Tìm người dùng theo tên đăng nhập
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Nếu đúng: hiển thị message box đăng nhập thành công → chuyển đến home.html
            model.addAttribute("success", "Đăng nhập thành công!");
            return "redirect:/home";
        } else {
            // Nếu sai: báo lỗi
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
            return "auth/login";
        }
    }

    // Hiển thị trang chủ (khi đăng nhập thành công)
    @GetMapping("/home")
    public String showHome() {
        return "guest/home"; // trỏ đến home.html trong templates/
    }
}
