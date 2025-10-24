package fourpetals.com.controller.auth;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fourpetals.com.dto.request.auth.LoginRequest;
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

    // -------------------- LOGIN VIEW --------------------
    @GetMapping("/login")
    public String showLogin(@CookieValue(value = "JWT_TOKEN", required = false) String jwtToken) {

        if (jwtToken == null || !tokenProvider.validateToken(jwtToken)) {
            return "auth/login";
        }

        String username = tokenProvider.getUsernameFromToken(jwtToken);
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return "auth/login"; // không tìm thấy user → hiển thị login
        }

        User user = userOpt.get();

        // Redirect theo role
        return switch (user.getRole().getRoleName()) {
            case ADMIN -> "redirect:/admin/dashboard";
            case MANAGER -> "redirect:/manager/dashboard";
            case SALES_EMPLOYEE -> "redirect:/sale/orders";
            case INVENTORY_EMPLOYEE -> "redirect:/inventory/dashboard";
            case SHIPPER -> "redirect:/shipper/process";
            case CUSTOMER -> "redirect:/home";
            default -> "auth/login"; // fallback
        };
    }


    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        Model model,
                        HttpServletResponse response) {

        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng / Tài khoản không thể đăng nhập / Tài khoản ngừng hoạt động!");
            return "auth/login";
        }

        User user = userOpt.get();
        if (user.getStatus() == -1) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên.");
            return "auth/login";
        }

        // Sinh access token
        String accessToken = tokenProvider.generateToken(user.getUsername());
        Cookie accessCookie = new Cookie("JWT_TOKEN", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(24 * 60 * 60); // 1 ngày
        response.addCookie(accessCookie);

        // Sinh refresh token
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());
        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(refreshCookie);

        // Redirect theo role
        return switch (user.getRole().getRoleName()) {
            case ADMIN -> "redirect:/admin/users";
            case MANAGER -> "redirect:/manager/dashboard";
            case SALES_EMPLOYEE -> "redirect:/sale/orders";
            case INVENTORY_EMPLOYEE -> "redirect:/inventory/dashboard";
            case SHIPPER -> "redirect:/shipper/process";
            case CUSTOMER -> "redirect:/home";
            default -> "redirect:/auth/login";
        };
    }

    // -------------------- REFRESH TOKEN --------------------
    @GetMapping("/refresh")
    public String refreshToken(HttpServletResponse response,
                               @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken) {

        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
            return "redirect:/login";
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = tokenProvider.generateToken(username);

        Cookie accessCookie = new Cookie("JWT_TOKEN", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(24 * 60 * 60); // 1 ngày
        response.addCookie(accessCookie);

        return "redirect:/home"; // Hoặc redirect về URL cũ nếu lưu trước đó
    }

    // -------------------- LOGOUT --------------------
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("JWT_TOKEN", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        SecurityContextHolder.clearContext();

        return "redirect:/home";
    }
}
