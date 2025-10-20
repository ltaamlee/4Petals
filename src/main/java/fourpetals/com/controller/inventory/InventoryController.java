package fourpetals.com.controller.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Order;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.InventoryService;
import fourpetals.com.service.MaterialService;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.SupplierService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

	private final UserService userService; // Lấy mã nhân viên từ user đăng nhập
	private final MaterialService materialService;
	private final SupplierService supplierService;
	private final OrderService orderService;
	private final InventoryService inventoryService;

	public InventoryController(UserService userService, MaterialService materialService,
			SupplierService supplierService, OrderService orderService, InventoryService inventoryService) {
		super();
		this.userService = userService;
		this.materialService = materialService;
		this.supplierService = supplierService;
		this.orderService = orderService;
		this.inventoryService = inventoryService;
	}

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		List<Material> listMaterials = materialService.findAll();
		List<Supplier> listSuppliers = supplierService.findAll();

		model.addAttribute("listSuppliers", listSuppliers);
		model.addAttribute("listMaterials", listMaterials);
		return "inventory/dashboard";
	}

	// Trang danh sách đơn hàng
	@GetMapping("/orders")
	public String orderList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		List<Order> listOrders = orderService.findAllConfirmedOrders();
		model.addAttribute("listOrders", listOrders);
		return "inventory/orders";
	}

	// Trang nhà cung cấp
	@GetMapping("/suppliers")
	public String supplierList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		model.addAttribute("materials", materialService.findAll());
		return "inventory/suppliers";
	}
	//
}