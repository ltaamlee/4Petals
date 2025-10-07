package fourpetals.com.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fourpetals.com.entity.User;
import fourpetals.com.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String usernameOrEmail,
                               @RequestParam("password") String password,
                               @RequestParam(value = "remember", required = false) String remember,
                               HttpSession session,
                               HttpServletResponse response,
                               Model model) {

        Optional<User> userOpt = userService.login(usernameOrEmail, password);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác / Tài khoản không thể đăng nhập");
            return "auth/login";
        }

        User user = userOpt.get();

        session.setAttribute("currentUser", user);

        if ("on".equals(remember)) {
            Cookie cookie = new Cookie("username", user.getUsername());
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        String redirectUrl;
        switch (user.getRole().getRoleName()) {
            case ADMIN -> redirectUrl = "redirect:/admin/dashboard";
            case MANAGER -> redirectUrl = "redirect:/manager/dashboard";
            case SALES_EMPLOYEE -> redirectUrl = "redirect:/sales/dashboard";
            case INVENTORY_EMPLOYEE -> redirectUrl = "redirect:/inventory/dashboard";
            case SHIPPER -> redirectUrl = "redirect:/shipper/dashboard";
            case CUSTOMER -> redirectUrl = "redirect:/home";
            default -> redirectUrl = "redirect:/login";
        }
        return redirectUrl;

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie("username", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
