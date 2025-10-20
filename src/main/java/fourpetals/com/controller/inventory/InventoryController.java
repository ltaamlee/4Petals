package fourpetals.com.controller.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Order;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;
import fourpetals.com.entity.User;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.InventoryService;
import fourpetals.com.service.MaterialService;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.SupplierService;
import fourpetals.com.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

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

	// LẤY DANH SÁCH PHIẾU NHẬP VÀ CHI TIẾT PHIẾU NHẬP
//	@GetMapping("/stores")
//	public String showInventoryPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//		if (userDetails != null) {
//			System.out.println("Username: " + userDetails.getUsername());
//			System.out.println("Authorities: " + userDetails.getAuthorities());
//			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
//			userOpt.ifPresent(user -> model.addAttribute("user", user));
//		} else {
//			System.out.println("userDetails is null");
//		}
//
//		List<Supplier> dsNCC = supplierService.findAll();
//
//		List<Inventory> listPhieuNhap = inventoryService.findAll();
//		List<InventoryDetail> listChiTiet = inventoryDetailService.findAllWithMaterialAndInventory();   // VIẾT InventoryDetailService và InventoryServiceImpl
//
//		// Map mã phiếu nhập -> danh sách nguyên liệu của nhà cung cấp
//		Map<Integer, List<Material>> mapNguyenLieu = new HashMap<>();
//		for (Inventory pn : listPhieuNhap) {
//			Supplier ncc = pn.getNhaCungCap();
//			List<Material> dsNL = List.of();
//			if (ncc != null) {
//				Supplier supplierWithMaterials = supplierService.findByIdWithMaterials(ncc.getMaNCC()).orElse(null);
//				if (supplierWithMaterials != null) {
//					dsNL = supplierWithMaterials.getNhaCungCapNguyenLieu().stream().map(SupplierMaterial::getNguyenLieu)
//							.toList();
//				}
//			}
//			mapNguyenLieu.put(pn.getMaPN(), dsNL);
//		}
//
//		model.addAttribute("listPhieuNhap", listPhieuNhap);
//		model.addAttribute("listChiTiet", listChiTiet);
//		model.addAttribute("dsNCC", dsNCC);
//		model.addAttribute("mapNguyenLieu", mapNguyenLieu);
//		System.out.println("listChiTiet size = " + listChiTiet.size());
//
//		return "inventory/add";
//	}
}