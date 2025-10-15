package fourpetals.com.controller.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.CustomerService;
import fourpetals.com.service.EmployeeService;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CustomerService customerService;

	// Thống kê doanh thu - nhân viên - khách hàng - đơn hàng
	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/dashboard";
	}

	// Quản lý nhân viên
	@GetMapping("/employees")
	public String employees(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		model.addAttribute("roles", roleService.findAll());

		return "manager/employees";
	}

	// Quản lý khách hàng
	@GetMapping("/customers")
	public String customer(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		model.addAttribute("genders", Gender.values());
		model.addAttribute("ranks", CustomerRank.values());
		return "manager/customers";
	}

	// Quản lý danh mục
	@GetMapping("/categories")
	public String category(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		return "manager/categories";
	}

	// Quản lý sản phẩm
	@GetMapping("/products")
	public String product(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		return "manager/products";
	}

	// Quản lý đơn hàng
	@GetMapping("/orders")
	public String orders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		return "manager/orders";
	}

	// Quản lý khuyến mãi
	@GetMapping("/promotions")
	public String promotion(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/promotions";
	}

	// Profile cá nhân
	@GetMapping("/profile")
	public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			if (userOpt.isPresent()) {
				model.addAttribute("user", userOpt.get());
			} else {
				model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
				return "manager/error";
			}
		}
		return "manager/profile";
	}

}
