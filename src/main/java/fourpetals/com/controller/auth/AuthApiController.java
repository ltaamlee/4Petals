//package fourpetals.com.controller.auth;
//
//import fourpetals.com.dto.request.auth.LoginRequest;
//import fourpetals.com.dto.response.auth.LoginResponse;
//import fourpetals.com.security.jwt.JwtTokenProvider;
//import fourpetals.com.entity.User;
//import fourpetals.com.service.UserService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthApiController {
//
//    private final UserService userService;
//    private final JwtTokenProvider tokenProvider;
//
//    public AuthApiController(UserService userService, JwtTokenProvider tokenProvider) {
//        this.userService = userService;
//        this.tokenProvider = tokenProvider;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Tên đăng nhập hoặc mật khẩu không đúng / tài khoản không hợp lệ!");
//        }
//
//        User user = userOpt.get();
//        if (user.getStatus() == -1) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Tài khoản đã bị khóa. Liên hệ quản trị viên!");
//        }
//
//        // Tạo access token + refresh token
//        String accessToken = tokenProvider.generateToken(user.getUsername());
//        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());
//
//        // Lưu refresh token vào cookie HttpOnly
//        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setPath("/");
//        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
//        response.addCookie(refreshCookie);
//
//        // Tạo LoginResponse
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setUserId(user.getUserId());
//        loginResponse.setUsername(user.getUsername());
//        loginResponse.setEmail(user.getEmail());
//        loginResponse.setRoleName(user.getRole().getRoleName().name());
//        loginResponse.setToken(accessToken);
//        loginResponse.setImageUrl(user.getImageUrl());
//
//        return ResponseEntity.ok(loginResponse);
//    }
//
//    // ♻️ Refresh access token từ cookie REFRESH_TOKEN
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken) {
//        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Refresh token không hợp lệ hoặc đã hết hạn.");
//        }
//
//        String username = tokenProvider.getUsernameFromToken(refreshToken);
//        String newAccessToken = tokenProvider.generateToken(username);
//
//        return ResponseEntity.ok(new LoginResponse() {{
//            setToken(newAccessToken);
//        }});
//    }
//
//    // 🚪 Logout -> xóa cookie refresh token
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//        Cookie clearCookie = new Cookie("REFRESH_TOKEN", null);
//        clearCookie.setHttpOnly(true);
//        clearCookie.setPath("/");
//        clearCookie.setMaxAge(0);
//        response.addCookie(clearCookie);
//
//        return ResponseEntity.ok("Đăng xuất thành công.");
//    }
//}
