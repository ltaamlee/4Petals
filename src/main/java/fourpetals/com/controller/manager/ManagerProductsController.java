package fourpetals.com.controller.manager;

import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.model.ProductRowVM;
import fourpetals.com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/manager/products")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerProductsController {

	@Autowired
	private ProductService productService;

	public record StatusOptionDTO(int value, String text) {
	}

	// ================== LIST (no paging) ==================
	@GetMapping("/no-paging")
	public List<ProductRowVM> listNoPaging(@RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) Integer status, @RequestParam(required = false) Integer categoryId) {
		return productService.searchNoPaging(keyword, status, categoryId);
	}

	// ================== STATUS OPTIONS cho modal (Thêm/Sửa) ==================
	@GetMapping("/statuses")
	public List<StatusOptionDTO> statuses() {
		return Arrays.stream(ProductStatus.values()).map(s -> new StatusOptionDTO(s.getValue(), s.getDisplayName()))
				.toList();
	}

	// ================== DETAIL (mở modal Sửa) ==================
	@GetMapping("/{maSP}")
	public ResponseEntity<ProductDetailResponse> detail(@PathVariable Integer maSP) {
		return productService.getDetail(maSP).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// ================== CREATE (JSON) ==================
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<ProductDetailResponse> create(@RequestBody ProductRequest req) {
		Integer id = productService.create(req);
		ProductDetailResponse body = productService.findById(id);
		return ResponseEntity.created(URI.create("/api/manager/products/" + id)).body(body);
	}

	@PostMapping(value = "/{maSP}/image", consumes = "multipart/form-data")
	public ResponseEntity<Void> uploadImage(@PathVariable Integer maSP, @RequestPart("file") MultipartFile file) {
		productService.updateImage(maSP, file);
		return ResponseEntity.ok().build();
	}

	// ================== UPDATE (JSON) ==================
	@PutMapping(value = "/{maSP}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ProductDetailResponse> update(@PathVariable Integer maSP, @RequestBody ProductRequest req) {
		productService.update(maSP, req);
		ProductDetailResponse body = productService.findById(maSP);
		return ResponseEntity.ok(body);
	}

	// ================== DELETE ==================
	@DeleteMapping("/{maSP}")
	public ResponseEntity<Void> delete(@PathVariable Integer maSP) {
		productService.delete(maSP);
		return ResponseEntity.ok().build();
	}
}
