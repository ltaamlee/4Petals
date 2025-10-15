package fourpetals.com.controller.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Supplier;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import fourpetals.com.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor

public class InventoryDetailController {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	 @GetMapping("/stores")
	    public String showInventoryPage(Model model) {
	        List<Supplier> dsNCC = supplierRepository.findAll();
	        List<Inventory> listPhieuNhap = inventoryRepository.findAll();

	        // giả sử nhân viên đăng nhập cố định (sau này bạn thay bằng session login)
	        Employee nv = employeeRepository.findAll().isEmpty() ? null : employeeRepository.findAll().get(0);
	        if (nv != null) {
	            model.addAttribute("tenNV", nv.getHoTen());
	            model.addAttribute("maNV", nv.getMaNV());
	        }

	        model.addAttribute("dsNCC", dsNCC);
	        model.addAttribute("listPhieuNhap", listPhieuNhap);
	        return "inventory/add"; // vì form nằm trong add.html
	    }

	    // ✅ Lưu phiếu nhập
	    @PostMapping("/add")
	    public String addPhieuNhap(@RequestParam("ngayNhap") LocalDate ngayNhap,
	                               @RequestParam(value = "tongTien", required = false) BigDecimal tongTien,
	                               @RequestParam("maNCC") Integer maNCC,
	                               @RequestParam("maNV") Integer maNV) {

	        Inventory phieuNhap = new Inventory();
	        phieuNhap.setNgayNhap(ngayNhap);
	        phieuNhap.setTongTien(tongTien != null ? tongTien : BigDecimal.ZERO);

	        Supplier ncc = supplierRepository.findById(maNCC).orElse(null);
	        phieuNhap.setNhaCungCap(ncc);

	        Employee nv = employeeRepository.findById(maNV).orElse(null);
	        phieuNhap.setNhanVien(nv);

	        inventoryRepository.save(phieuNhap);

	        // ✅ Sau khi lưu thì quay lại trang chính
	        return "redirect:/inventory/stores";
	    }
}
