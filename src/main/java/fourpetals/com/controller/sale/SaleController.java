package fourpetals.com.controller.sale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/sale")
@PreAuthorize("hasRole('SALES_EMPLOYEE')")
public class SaleController {

	private final UserService userService;
	private final RoleService roleService;
	private final OrderService orderService;

	public SaleController(UserService userService, RoleService roleService, OrderService orderService) {
		super();
		this.userService = userService;
		this.roleService = roleService;
		this.orderService = orderService;
	}

	// Đơn hàng
	@GetMapping("/orders")
	public String order(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			System.out.println("Username: " + userDetails.getUsername());
			System.out.println("Authorities: " + userDetails.getAuthorities());

			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				model.addAttribute("user", user);
				
				Employee nhanVien = user.getNhanVien(); 

				if (nhanVien != null) {
					model.addAttribute("currentEmployeeId", nhanVien.getMaNV()); // id nhân viên
					model.addAttribute("currentEmployeeName", nhanVien.getHoTen()); // tên nhân viên
				} else {
					System.out.println("User chưa gán nhân viên");
				}
			} else {
				System.out.println("Không tìm thấy user trong DB");
			}
		} else {
			System.out.println("userDetails is null");
		}

		// Load trạng thái đơn hàng
		model.addAttribute("orderStatuses", OrderStatus.values());

		// Thống kê đơn hàng
		LocalDateTime todayStart = LocalDate.now().atStartOfDay();
		LocalDateTime todayEnd = todayStart.plusDays(1).minusNanos(1);

		long totalOrders = orderService.countByNgayDatBetween(todayStart, todayEnd);
		long pendingOrders = orderService.countByTrangThai(OrderStatus.CHO_XU_LY);
		long completedOrders = orderService.countByTrangThai(OrderStatus.DA_XAC_NHAN);

		model.addAttribute("totalOrders", totalOrders);
		model.addAttribute("pendingOrders", pendingOrders);
		model.addAttribute("completedOrders", completedOrders);

		return "sale/orders";
	}

	// Sản phẩm - Xem - Lọc - Tìm kiếm - Sắp xếp
	@GetMapping("/products")
	public String product(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			System.out.println("Username: " + userDetails.getUsername());
			System.out.println("Authorities: " + userDetails.getAuthorities());
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		} else {
			System.out.println("userDetails is null");
		}

		return "sale/products";
	}

	// Profile
	@GetMapping("/profile")
	public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			if (userOpt.isPresent()) {
				model.addAttribute("user", userOpt.get());
			} else {
				model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
				return "sale/error";
			}
		}
		return "sale/profile";
	}

}
