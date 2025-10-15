package fourpetals.com.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.request.supplier.SupplierRequest;
import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Supplier;
import fourpetals.com.mapper.SupplierMapping;
import fourpetals.com.service.SupplierService;

@RestController
@RequestMapping("/api/admin/suppliers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSupplierController {

	@Autowired
	private SupplierService supplierService;

	// --- Danh sách nhà cung cấp + search + pagination ---
	@GetMapping
	public Page<SupplierResponse> getSuppliers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) Integer materialId) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("maNCC").ascending());
		Page<Supplier> suppliersPage;

		if (materialId != null && materialId > 0) {
			suppliersPage = (keyword == null || keyword.isBlank())
					? supplierService.findSuppliersByMaterial(materialId, pageable)
					: supplierService.searchSuppliersByMaterial(materialId, keyword, pageable);
		} else {
			suppliersPage = (keyword == null || keyword.isBlank()) ? supplierService.findAll(pageable)
					: supplierService.search(keyword, pageable);
		}

		return suppliersPage.map(SupplierMapping::toSupplierResponse);
	}

	// -------------------- Xem chi tiết nhà cung cấp --------------------
	@GetMapping("/view/{id}")
	public ResponseEntity<SupplierResponse> getSupplierDetail(@PathVariable Integer id) {
		SupplierResponse dto = supplierService.getSupplierDetail(id);
		return ResponseEntity.ok(dto);
	}

	// --- Thêm nhà cung cấp mới ---
	@PostMapping("/add")
	public ResponseEntity<SupplierResponse> addSupplier(@RequestBody SupplierRequest request) {
		Supplier supplierWithMaterials = supplierService.create(request);
		SupplierResponse response = SupplierMapping.toSupplierResponseDetail(supplierWithMaterials);
		return ResponseEntity.ok(response);
	}

	// --- Cập nhật nhà cung cấp ---
	@PutMapping("/edit/{id}")
	public ResponseEntity<SupplierResponse> updateSupplier(
	        @PathVariable Integer id,
	        @RequestBody SupplierRequest request) {

	    request.setMaNCC(id);
	    Supplier updated = supplierService.update(request); 
	    SupplierResponse response = SupplierMapping.toSupplierResponse(updated); 
	    return ResponseEntity.ok(response); 
	}


	// Xóa nhà cung cấp
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSupplier(@PathVariable("id") Integer id) {
		try {
			supplierService.delete(id);
			System.out.println("Đang xóa NCC: " + id); // debug Java
			return ResponseEntity.ok("Xóa nhà cung cấp thành công!");
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã có lỗi xảy ra khi xóa nhà cung cấp.");
		}
	}

}
