package fourpetals.com.controller.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.User;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

	private final MaterialRepository materialRepository;
	private final SupplierRepository supplierRepository;
	private final UserService userService;

	public InventoryController(MaterialRepository materialRepository, SupplierRepository supplierRepository,
			UserService userService) {
		super();
		this.materialRepository = materialRepository;
		this.supplierRepository = supplierRepository;
		this.userService = userService;
	}

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		List<Material> listMaterials = materialRepository.findAll();
		List<Supplier> listSuppliers = supplierRepository.findAll();

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
		return "inventory/orders"; // templates/inventory/orders.html
	}

	// Trang nhà cung cấp
	@GetMapping("/suppliers")
	public String supplierList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		model.addAttribute("materials", materialRepository.findAll());
		return "inventory/suppliers";
	}
}