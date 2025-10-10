package fourpetals.com.controller.auth;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fourpetals.com.dto.request.LoginRequest;
import fourpetals.com.entity.User;
import fourpetals.com.security.jwt.JwtTokenProvider;
import fourpetals.com.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        Model model,
                        HttpServletResponse response) {

        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng / Tài khoản không thể đăng nhập");
            return "auth/login";
        }

        User user = userOpt.get();
        String token = tokenProvider.generateToken(user.getUsername());

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); 
        response.addCookie(cookie);

        return switch (user.getRole().getRoleName()) {
            case ADMIN -> "redirect:/admin/dashboard";
            case MANAGER -> "redirect:/manager/dashboard";
            case SALES_EMPLOYEE -> "redirect:/sales/dashboard";
            case INVENTORY_EMPLOYEE -> "redirect:/inventory/dashboard";
            case SHIPPER -> "redirect:/shipper/dashboard";
            case CUSTOMER -> "redirect:/home";
            default -> "redirect:/auth/login";
        };
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return "redirect:/home";
    }

}