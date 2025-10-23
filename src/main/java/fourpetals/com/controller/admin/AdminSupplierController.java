package fourpetals.com.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.opencsv.CSVWriter;

import fourpetals.com.dto.request.supplier.SupplierRequest;
import fourpetals.com.dto.response.stats.SupplierStatsResponse;
import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Supplier;
import fourpetals.com.enums.SupplierStatus;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.mapper.SupplierMapping;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.service.SupplierService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/suppliers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSupplierController {

	@Autowired
	private SupplierService supplierService;

	@GetMapping("/stats")
	public SupplierStatsResponse getSupplierStats() {
		// Gọi trực tiếp service đã có
		return supplierService.getSupplierStats();
	}

	// --- Danh sách nhà cung cấp + search + pagination ---
	@GetMapping
	public Map<String, Object> getSuppliers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) SupplierStatus status, @RequestParam(required = false) Integer materialId) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("maNCC").ascending());
		Page<Supplier> suppliersPage = supplierService.searchSuppliers(keyword, materialId, status, pageable);

		Map<String, Object> response = Map.of("content",
				suppliersPage.map(SupplierMapping::toSupplierResponse).getContent(), "totalPages",
				suppliersPage.getTotalPages(), "number", suppliersPage.getNumber(), "size", suppliersPage.getSize(),
				"totalElements", suppliersPage.getTotalElements());

		return response;
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<?> updateSupplierStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
		String statusStr = payload.get("status");
		try {
			SupplierStatus newStatus = SupplierStatus.valueOf(statusStr.toUpperCase());
			Supplier updated = supplierService.updateStatus(id, newStatus);
			return ResponseEntity.ok(updated);
		} catch (IllegalArgumentException | NullPointerException e) {
			return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nhà cung cấp không tồn tại");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

	// -------------------- Xem chi tiết nhà cung cấp --------------------
	@GetMapping("/view/{id}")
	public ResponseEntity<SupplierResponse> getSupplierDetail(@PathVariable Integer id) {
		SupplierResponse dto = supplierService.getSupplierDetail(id);
		return ResponseEntity.ok(dto);
	}

	// --- Thêm nhà cung cấp mới ---
	@PostMapping("/add")
	public ResponseEntity<SupplierResponse> addSupplier(@Valid @RequestBody SupplierRequest request) {
		Supplier supplierWithMaterials = supplierService.create(request);
		SupplierResponse response = SupplierMapping.toSupplierResponseDetail(supplierWithMaterials);
		return ResponseEntity.ok(response);
	}

	// --- Cập nhật nhà cung cấp ---
	@PutMapping("/edit/{id}")
	public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Integer id,
			@Valid @RequestBody SupplierRequest request) {

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

	@Autowired
	private SupplierRepository supplierRepository;

	@GetMapping("/export/csv")
	public void exportSuppliersData(HttpServletResponse response) throws IOException {
	    response.setCharacterEncoding("UTF-8");

	    // ✅ Lấy toàn bộ supplier cùng nguyên liệu (fetch sẵn, không lazy)
	    List<Supplier> suppliers = supplierRepository.findAllWithMaterials();

	    // ⚠️ Nếu không có dữ liệu, trả JSON để phía JS hiển thị alert
	    if (suppliers == null || suppliers.isEmpty()) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.setContentType("application/json; charset=UTF-8");
	        response.getWriter().write("{\"message\": \"Không có nhà cung cấp để xuất.\"}");
	        return;
	    }

	    // ✅ Nếu có dữ liệu thì trả file CSV
	    response.setContentType("text/csv; charset=UTF-8");
	    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"danh_sach_nha_cung_cap.csv\"");

	    // Ghi BOM để Excel đọc UTF-8
	    response.getWriter().write('\uFEFF');

	    // Mapping sang DTO
	    List<SupplierResponse> supplierResponses = suppliers.stream()
	            .map(SupplierMapping::toSupplierResponseDetail)
	            .toList();

	    // ✅ Ghi file CSV
	    try (CSVWriter csvWriter = new CSVWriter(response.getWriter())) {
	        String[] header = { "MãNCC", "TênNCC", "Email", "Số điện thoại", "Địa Chỉ", "Trạng thái", "Mô tả",
	                            "Ngày tạo", "Ngày cập nhật", "Nguyên liệu cung cấp" };
	        csvWriter.writeNext(header);

	        for (SupplierResponse s : supplierResponses) {
	            String materials = (s.getNhaCungCapNguyenLieuNames() != null)
	                    ? String.join(";", s.getNhaCungCapNguyenLieuNames())
	                    : "";

	            csvWriter.writeNext(new String[]{
	                    String.valueOf(s.getMaNCC()),
	                    s.getTenNCC(),
	                    s.getEmail(),
	                    s.getSdt(),
	                    s.getDiaChi(),
	                    s.getTrangThai() != null ? s.getTrangThai().name() : "",
	                    s.getMoTa(),
	                    s.getCreatedAt() != null ? s.getCreatedAt().toString() : "",
	                    s.getUpdatedAt() != null ? s.getUpdatedAt().toString() : "",
	                    materials
	            });
	        }
	    } catch (Exception e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.setContentType("application/json; charset=UTF-8");
	        response.getWriter().write("{\"message\": \"Lỗi khi xuất file CSV: " + e.getMessage() + "\"}");
	    }
	}

}
