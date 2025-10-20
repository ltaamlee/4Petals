package fourpetals.com.controller.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;
import fourpetals.com.entity.User;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory/detail")
@RequiredArgsConstructor
// CHUYỂN SANG RESET CONTROLLER 
public class InventoryDetailController {
	
	
    // DÙNG SERVIVE KHÔNG GỌI TỪ REPOSITORY LÊN
	@Autowired 
	private InventoryRepository inventoryRepository;
	@Autowired
	private InventoryDetailRepository inventoryDetailRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private UserService userService;

	

	// Lưu phiếu nhập
	@PostMapping("/add")
	public String addPhieuNhap(@RequestParam("ngayNhap") LocalDate ngayNhap,
			@RequestParam(value = "tongTien", required = false) BigDecimal tongTien,
			@RequestParam("maNCC") Integer maNCC/* , @RequestParam("maNV") Integer maNV */) {

		Inventory phieuNhap = new Inventory();
		phieuNhap.setNgayNhap(ngayNhap);
		phieuNhap.setTongTien(tongTien != null ? tongTien : BigDecimal.ZERO);

		Supplier ncc = supplierRepository.findById(maNCC).orElse(null);
		phieuNhap.setNhaCungCap(ncc);

		Employee nv = employeeRepository.findAll().get(0);

		phieuNhap.setNhanVien(nv);

		inventoryRepository.save(phieuNhap);

		return "redirect:/inventory/stores";
	}

	// Xóa phiếu nhập
	@PostMapping("/delete")
	@Transactional
	public String deletePhieuNhap(@RequestParam("maPN") Integer maPN) {
		Inventory phieuNhap = inventoryRepository.findById(maPN).orElse(null);
		if (phieuNhap != null) {
			List<InventoryDetail> chiTiets = inventoryDetailRepository.findByPhieuNhap(phieuNhap);
			for (InventoryDetail ct : chiTiets) {
				Material nguyenLieu = ct.getNguyenLieu();
				nguyenLieu.setSoLuongTon(Math.max(0, nguyenLieu.getSoLuongTon() - ct.getSoLuong()));
				materialRepository.save(nguyenLieu);

			}
			inventoryDetailRepository.deleteAll(chiTiets);
			inventoryRepository.delete(phieuNhap);
		}
		return "redirect:/inventory/stores";
	}

	// Lưu chi tiết phiếu nhập
	@PostMapping("/detail/add")
	public String addChiTietPhieuNhap(@RequestParam("phieuNhap") Integer maPN, @RequestParam("nguyenLieu") Integer maNL,
			@RequestParam("soLuong") Integer soLuong, @RequestParam("giaNhap") BigDecimal giaNhap) {

		InventoryDetail chiTiet = new InventoryDetail();
		Inventory pn = inventoryRepository.findById(maPN).orElse(null);
		if (pn == null)
			return "redirect:/inventory/stores";

		Material nguyenLieu = materialRepository.findById(maNL).orElse(null);
		if (nguyenLieu == null)
			return "redirect:/inventory/stores";

		chiTiet.setPhieuNhap(pn);
		chiTiet.setNguyenLieu(nguyenLieu);
		chiTiet.setSoLuong(soLuong);
		chiTiet.setGiaNhap(giaNhap);

		inventoryDetailRepository.save(chiTiet);

		List<InventoryDetail> chiTiets = inventoryDetailRepository.findByPhieuNhap(pn);
		BigDecimal tongTien = chiTiets.stream().map(ct -> ct.getGiaNhap().multiply(new BigDecimal(ct.getSoLuong())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		pn.setTongTien(tongTien);
		inventoryRepository.save(pn);

		// Cập nhật số lượng tồn của nguyên liệu
		nguyenLieu.setSoLuongTon(nguyenLieu.getSoLuongTon() + soLuong);
		materialRepository.save(nguyenLieu);

		return "redirect:/inventory/stores";
	}

	// Xóa chi tiết phiếu nhập
	@PostMapping("/detail/delete")   //ĐỔI THÀNH DELETE MAPPING
	@Transactional
	public String deleteChiTietPhieuNhap(@RequestParam("maCTPN") Integer maCTPN) {
		InventoryDetail chiTiet = inventoryDetailRepository.findById(maCTPN).orElse(null);
		if (chiTiet != null) {
			Inventory phieuNhap = chiTiet.getPhieuNhap();
			Material nguyenLieu = chiTiet.getNguyenLieu();

			// Giảm số lượng tồn của nguyên liệu
			nguyenLieu.setSoLuongTon(Math.max(0, nguyenLieu.getSoLuongTon() - chiTiet.getSoLuong()));
			materialRepository.save(nguyenLieu);

			// Cập nhật tổng tiền phiếu nhập
			BigDecimal thanhTien = chiTiet.getGiaNhap().multiply(BigDecimal.valueOf(chiTiet.getSoLuong()));
			if (phieuNhap.getTongTien() != null) {
				phieuNhap.setTongTien(phieuNhap.getTongTien().subtract(thanhTien));
			}
			inventoryRepository.save(phieuNhap);

			inventoryDetailRepository.delete(chiTiet);
		}
		return "redirect:/inventory/stores";
	}

	// Cập nhật chi tiết phiếu nhập
	@PostMapping("/detail/edit")
	@Transactional
	public String updateChiTietPhieuNhap(@RequestParam("maCTPN") Integer maCTPN, @RequestParam("maNL") Integer maNL,
			@RequestParam("soLuong") Integer soLuong, @RequestParam("giaNhap") BigDecimal giaNhap) {

		InventoryDetail ct = inventoryDetailRepository.findById(maCTPN).orElse(null);
		if (ct != null) {
			Inventory phieuNhap = ct.getPhieuNhap();
			Material oldMaterial = ct.getNguyenLieu();
			int oldSoLuong = ct.getSoLuong();

			Material newMaterial = materialRepository.findById(maNL).orElse(oldMaterial);

			// Cập nhật số lượng tồn
			if (oldMaterial.getMaNL().equals(newMaterial.getMaNL())) {
				int diff = soLuong - oldSoLuong;
				newMaterial.setSoLuongTon(Math.max(0, newMaterial.getSoLuongTon() + diff));
				materialRepository.save(newMaterial);
			} else {
				oldMaterial.setSoLuongTon(Math.max(0, oldMaterial.getSoLuongTon() - oldSoLuong));
				newMaterial.setSoLuongTon(Math.max(0, newMaterial.getSoLuongTon() + soLuong));
				materialRepository.save(oldMaterial);
				materialRepository.save(newMaterial);
			}

			ct.setNguyenLieu(newMaterial);
			ct.setSoLuong(soLuong);
			ct.setGiaNhap(giaNhap);
			inventoryDetailRepository.save(ct);

			// Cập nhật tổng tiền phiếu nhập
			BigDecimal tongTienMoi = inventoryDetailRepository.findByPhieuNhap(phieuNhap).stream()
					.map(detail -> detail.getGiaNhap().multiply(BigDecimal.valueOf(detail.getSoLuong())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			phieuNhap.setTongTien(tongTienMoi);
			inventoryRepository.save(phieuNhap);
		}

		return "redirect:/inventory/stores";
	}

	// Xem chi tiết phiếu nhập

	@GetMapping("/detail/{maPN}")
	public List<InventoryDetail> getChiTietPhieuNhap(@PathVariable Integer maPN) {
		return inventoryDetailRepository.findByPhieuNhapWithMaterial(maPN);
	}

}
