package fourpetals.com.controller.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.User;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

	private final MaterialRepository materialRepository;
	private final SupplierRepository supplierRepository;
	@Autowired
	private UserService userService;

	public InventoryController(MaterialRepository materialRepository, SupplierRepository supplierRepository) {
		super();
		this.materialRepository = materialRepository;
		this.supplierRepository = supplierRepository;
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
	//
}