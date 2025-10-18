package fourpetals.com.controller.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.security.CustomUserDetails;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import fourpetals.com.enums.ProductStatus;
import fourpetals.com.service.*;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

	private final RoleService roleService;
	private final UserService userService;
	private final EmployeeService employeeService;
	private final CustomerService customerService;
	private final CategoryService categoryService;
	private final MaterialService materialService;

	public ManagerController(RoleService roleService, UserService userService, EmployeeService employeeService,
			CustomerService customerService, CategoryService categoryService, MaterialService materialService) {
		super();
		this.roleService = roleService;
		this.userService = userService;
		this.employeeService = employeeService;
		this.customerService = customerService;
		this.categoryService = categoryService;
		this.materialService = materialService;
	}

	// Thống kê doanh thu - nhân viên - khách hàng - đơn hàng
	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/dashboard";
	}

	// -------------------- Quản lý nhân viên --------------------
	@GetMapping("/employees")
	public String employees(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		model.addAttribute("roles", roleService.findAll());

		return "manager/employees";
	}

	// -------------------- Quản lý khách hàng --------------------
	@GetMapping("/customers")
	public String page(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(defaultValue = "") String keyword, @RequestParam(required = false) Gender gender,
			@RequestParam(required = false) CustomerRank rank, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {

		if (userDetails != null) {
			Optional<User> u = userService.findByUsername(userDetails.getUsername());
			u.ifPresent(user -> model.addAttribute("user", user));
		}

		Page<CustomerRowVM> rowsPage = customerService.searchRows(keyword, gender, rank, page, size);

		model.addAttribute("rows", rowsPage.getContent());
		model.addAttribute("totalCustomers", customerService.countAll());
		model.addAttribute("newMonthCustomers", customerService.countNewInCurrentMonth());
		model.addAttribute("femaleCustomers", customerService.countByGender(Gender.NU));
		model.addAttribute("maleCustomers", customerService.countByGender(Gender.NAM));
		model.addAttribute("genders", Gender.values());
		model.addAttribute("ranks", CustomerRank.values());
		model.addAttribute("keyword", keyword);
		model.addAttribute("gender", gender);
		model.addAttribute("rank", rank);

		return "manager/customers";
	}

	// -------------------- Quản lý danh mục --------------------
	@GetMapping("/categories")
	public String category(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		return "manager/categories";
	}

	// -------------------- Quản lý sản phẩm --------------------
	@GetMapping("/products")
	public String products(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(u -> model.addAttribute("user", u));
		}
		// ĐỔI DÒNG NÀY (findAll -> getAllCategories)
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("materials", materialService.findAll());
		model.addAttribute("statuses", ProductStatus.values());
		return "manager/products";
	}

	// -------------------- Quản lý đơn hàng--------------------
	@GetMapping("/orders")
	public String orders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		return "manager/orders";
	}

	// -------------------- Quản lý khuyến mãi --------------------
	@GetMapping("/promotions")
	public String promotion(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "manager/promotions";
	}

	// -------------------- Profile cá nhân --------------------
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