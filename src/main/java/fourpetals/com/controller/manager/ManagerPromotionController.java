package fourpetals.com.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.request.promotions.PromotionCreateRequest;
import fourpetals.com.dto.request.promotions.PromotionUpdateRequest;
import fourpetals.com.dto.response.promotions.PromotionResponse;
import fourpetals.com.dto.response.stats.PromotionStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Promotion;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;
import fourpetals.com.service.ProductBannerService;
import fourpetals.com.service.PromotionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/manager/promotions")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerPromotionController {

	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private ProductBannerService productBannerService;

	@GetMapping("/stats")
	public ResponseEntity<PromotionStatsResponse> getPromotionStats(
			@RequestParam(defaultValue = "7") int daysToExpire) {
		return ResponseEntity.ok(promotionService.getPromotionStats(daysToExpire));
	}

	@GetMapping
	public Page<PromotionResponse> getPromotions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) PromotionType type,
			@RequestParam(required = false) PromotionStatus status) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		return promotionService.searchPromotions(keyword, type, status, pageable);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePromotionStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {

		String statusStr = payload.get("status");
		PromotionStatus newStatus;

		try {
			newStatus = PromotionStatus.valueOf(statusStr.toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
		}

		try {
			Promotion updated = promotionService.updateStatus(id, newStatus);
			return ResponseEntity.ok(updated);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khuyến mãi không tồn tại");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getPromotionForProduct(
	        @PathVariable Integer productId,
	        @RequestParam(required = false) String loaiKH) {

	    CustomerRank rank = null;
	    if (loaiKH != null) {
	        try {
	            rank = CustomerRank.valueOf(loaiKH.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest()
	                    .body(Map.of("message", "Loại khách hàng không hợp lệ"));
	        }
	    }

	    Optional<PromotionResponse> promo = promotionService.getActivePromotionForProduct(productId, rank);

	    if (promo.isPresent()) {
	        return ResponseEntity.ok(promo.get());
	    } else {
	        return ResponseEntity.ok(Map.of("message", "Không có khuyến mãi cho sản phẩm"));
	    }
	}




	@GetMapping("/banners")
	public ResponseEntity<?> getAllProductBanners() {
		return ResponseEntity.ok(productBannerService.getAllBanners());
	}

	// Xem chi tiết khuyến mãi
	@GetMapping("/view/{id}")
	public ResponseEntity<?> viewPromotion(@PathVariable("id") Integer id) {
		PromotionResponse promotion = promotionService.getPromotionDetail(id);
		if (promotion == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(promotion);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addPromotion(@Valid @RequestBody PromotionCreateRequest request) {
		try {
			System.out.println(">>> Dữ liệu nhận được: " + request);
			if (request.getSanPhamIds() == null)
				request.setSanPhamIds(new ArrayList<>());
			PromotionResponse response = promotionService.createPromotion(request);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// Chỉnh sửa khuyến mãi
	@PutMapping("/edit/{id}")
	public ResponseEntity<?> editPromotion(@PathVariable Integer id,
			@Valid @RequestBody PromotionUpdateRequest request) {
		try {
			if (request.getSanPhamIds() == null)
				request.setSanPhamIds(new ArrayList<>());
			PromotionResponse updated = promotionService.updatePromotion(id, request);
			return ResponseEntity.ok(updated);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khuyến mãi không tồn tại");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePromotion(@PathVariable Integer id) {
		try {
			promotionService.deletePromotion(id);
			return ResponseEntity.ok().build();
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khuyến mãi không tồn tại");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

}
