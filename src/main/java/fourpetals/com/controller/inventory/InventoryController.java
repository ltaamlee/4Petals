package fourpetals.com.controller.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private SupplierRepository supplierRepository;
//	@Autowired
//	private InventoryRepository inventoryRepository;
//
//	@Autowired
//	private InventoryDetailRepository inventoryDetailRepository;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		List<Material> listMaterials = materialRepository.findAll();
		List<Supplier> listSuppliers = supplierRepository.findAll();

		model.addAttribute("listSuppliers", listSuppliers);
		model.addAttribute("listMaterials", listMaterials);
		return "inventory/dashboard";
	}

	// Trang nhập phiếu nhập (Inventory) → chỉ dẫn tới form tạo phiếu
	
//	 @GetMapping("/stores") public String addInventoryPage(Model model) {
//	 
//	 List<Inventory> listPhieuNhap = inventoryRepository.findAll();
//	 List<InventoryDetail> listChiTiet = inventoryDetailRepository.findAll();
//	 
//	 model.addAttribute("listPhieuNhap", listPhieuNhap);
//	  model.addAttribute("listChiTiet", listChiTiet); return "inventory/add"; }
	 //templates/inventory/add.html }
	 

	// Trang danh sách đơn hàng
	@GetMapping("/orders")
	public String orderList() {
		return "inventory/orders"; // templates/inventory/orders.html
	}
}