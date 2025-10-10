package fourpetals.com.controller.admin;

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
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    public AdminController(UserService userService) {
    	this.userService = userService;
    }

    //Thống kê tổng quan
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("totalUsers", 100);
        model.addAttribute("todayOrders", 25);
        model.addAttribute("revenue", 5000000);

        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }

        return "admin/dashboard";
    }

    //Quản lý tài khoản người dùng
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String users(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        return "admin/users";
    }

    //Phân quyền chức năng, tạo vai trò mới
    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public String permissions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        return "admin/permissions";
    }

    // Lịch sử hoạt động của các tài khoản
    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public String history(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        return "admin/history";
    }

    //Cấu hình hệ thống: Logo, Tên cửa hàng, Phương thức thanh toán
    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public String config(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        return "admin/config";
    }

    //Profile của Admin
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            if (userOpt.isPresent()) {
                model.addAttribute("user", userOpt.get());
            } else {
                model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
                return "admin/error";
            }
        }
        return "admin/profile";
    }
}
