package fourpetals.com.controller.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor

public class InventoryDetailController {

	@Autowired
	private InventoryRepository inventoryRepository;
	private InventoryDetailRepository inventoryDetailRepository;

	// Lưu Phiếu Nhập
	@PostMapping("/add")
	public String addPhieuNhap(Inventory phieuNhap) {
		inventoryRepository.save(phieuNhap);
		return "redirect:/inventory/stores"; // quay về trang nhập
	}

	// Lưu Chi Tiết Phiếu Nhập
	@PostMapping("/detail/add")
	public String addChiTiet(InventoryDetail chiTiet) {
		inventoryDetailRepository.save(chiTiet);
		return "redirect:/inventory/stores"; // quay về trang nhập
	}
}
