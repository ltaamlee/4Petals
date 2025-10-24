package fourpetals.com.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.dto.response.stats.SupplierStatsResponse;
import fourpetals.com.entity.User;
import fourpetals.com.enums.Gender;
import fourpetals.com.enums.SupplierStatus;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.MaterialService;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.SupplierService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final MaterialService materialService;
    @Autowired
    private SupplierService supplierService;
    
    public AdminController(UserService userService, RoleService roleService, MaterialService materialService) {
		super();
		this.userService = userService;
		this.roleService = roleService;
		this.materialService = materialService;
	}

	//Thống kê tổng quan
    @GetMapping("/dashboard")
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
    public String users(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("keyword", "");
        model.addAttribute("status", "");
        model.addAttribute("roleId", "");
        return "admin/users";
        
    }

    //Quản lý đối tác - nhà cung cấp nguyên liệu
    @GetMapping("/suppliers")
    public String supplier(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        SupplierStatsResponse stats = supplierService.getSupplierStats();

        model.addAttribute("totalSuppliers", stats.getTotalSuppliers());
        model.addAttribute("activeSuppliers", stats.getActiveSuppliers());
        model.addAttribute("inactiveSuppliers", stats.getInactiveSuppliers());
        model.addAttribute("blockedSuppliers", stats.getBlockedSuppliers());

        // Truyền thêm danh sách trạng thái và nguyên liệu để filter
        model.addAttribute("statuses", SupplierStatus.values());
        model.addAttribute("materials", materialService.findAll());

        return "admin/suppliers";
    }
    

    //Profile của Admin
    @GetMapping("/profile")
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
