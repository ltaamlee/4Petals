package fourpetals.com.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.dto.request.promotions.PromotionCreateRequest;
import fourpetals.com.dto.request.promotions.PromotionUpdateRequest;
import fourpetals.com.dto.response.promotions.PromotionResponse;
import fourpetals.com.dto.response.stats.PromotionStatsResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;
import fourpetals.com.mapper.PromotionMapping;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.repository.PromotionDetailRepository;
import fourpetals.com.repository.PromotionRepository;
import fourpetals.com.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private PromotionDetailRepository promotionDetailRepository;

	@Override
	public PromotionStatsResponse getPromotionStats(int daysToExpire) {
		long total = promotionRepository.count();
		long active = promotionRepository.countByTrangThai(PromotionStatus.ACTIVE);
		long inactive = promotionRepository.countByTrangThai(PromotionStatus.INACTIVE);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(daysToExpire);

		// ✅ Chỉ tính những khuyến mãi còn hiệu lực và sắp hết hạn
		long expiringSoon = promotionRepository.countByTrangThaiAndThoiGianKtBetween(PromotionStatus.ACTIVE, now,
				endDate);

		long expired = promotionRepository.countByTrangThai(PromotionStatus.EXPIRED);

		System.out.println("=== Promotion Stats Debug ===");
		System.out.println("Now: " + now);
		System.out.println("EndDate (now + daysToExpire): " + endDate);
		System.out.println("Total promotions: " + total);
		System.out.println("Active promotions: " + active);
		System.out.println("Inactive promotions: " + inactive);
		System.out.println("Expiring soon promotions: " + expiringSoon);
		System.out.println("=============================");

		return new PromotionStatsResponse(total, inactive, active, expiringSoon,  expired);
	}

	// ----------------- CRUD -----------------
	@Override
	@Transactional
	public PromotionResponse createPromotion(PromotionCreateRequest request) {
		// Kiểm tra loại khách hàng
		CustomerRank rank = request.getLoaiKhachHang();
		if (rank != null && !EnumSet
				.of(CustomerRank.BAC, CustomerRank.VANG, CustomerRank.KIM_CUONG, CustomerRank.THUONG).contains(rank)) {
			throw new RuntimeException("Loại khách hàng không hợp lệ: " + rank);
		}

		Promotion promotion = new Promotion();
		promotion.setTenkm(request.getTenkm());
		promotion.setLoaiKm(request.getLoaiKm());
		promotion.setTrangThai(request.getTrangThai() != null ? request.getTrangThai() : PromotionStatus.INACTIVE);
		promotion.setGiaTri(request.getGiaTri());
		promotion.setThoiGianBd(request.getThoiGianBd());
		promotion.setThoiGianKt(request.getThoiGianKt());
		promotion.setMoTa(request.getMoTa());

		Promotion saved = promotionRepository.save(promotion);

		// 3️⃣ Tạo danh sách PromotionDetail
		List<PromotionDetail> details = new ArrayList<>();

		// Nếu có danh sách sản phẩm
		if (request.getSanPhamIds() != null && !request.getSanPhamIds().isEmpty()) {
			for (Integer productId : request.getSanPhamIds()) {
				PromotionDetail detail = new PromotionDetail();
				detail.setKhuyenMai(saved);
				detail.setSanPham(productRepository.findById(productId)
						.orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + productId)));
				detail.setLoaiKhachHang(rank); // có thể null (áp dụng cho mọi khách hàng)
				details.add(detail);
			}
		} else {
			// Không chọn sản phẩm → áp dụng toàn shop
			PromotionDetail detail = new PromotionDetail();
			detail.setKhuyenMai(saved);
			detail.setSanPham(null); // áp dụng cho tất cả sản phẩm
			detail.setLoaiKhachHang(rank); // có thể null (mọi khách hàng)
			details.add(detail);
		}

		// 4️⃣ Lưu chi tiết khuyến mãi
		promotionDetailRepository.saveAll(details);
		saved.setChiTietKhuyenMais(details);

		// 5️⃣ Trả về DTO chi tiết
		return PromotionMapping.toPromotionResponseDetail(saved);
	}

	@Override
	@Transactional
	public PromotionResponse updatePromotion(Integer id, PromotionUpdateRequest request) {
		// 1️⃣ Lấy khuyến mãi hiện tại theo id
		Promotion promotion = promotionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi ID = " + id));

		// 2️⃣ Cập nhật thông tin cơ bản
		promotion.setTenkm(request.getTenkm());
		promotion.setLoaiKm(request.getLoaiKm());
		promotion.setGiaTri(request.getGiaTri());
		promotion.setThoiGianBd(request.getThoiGianBd());
		promotion.setThoiGianKt(request.getThoiGianKt());
		promotion.setMoTa(request.getMoTa());

		if (request.getTrangThai() != null) {
			promotion.setTrangThai(request.getTrangThai());
		}

		// 3️⃣ Kiểm tra loại khách hàng hợp lệ
		CustomerRank rank = request.getLoaiKhachHang();
		if (rank != null && !EnumSet
				.of(CustomerRank.THUONG, CustomerRank.BAC, CustomerRank.VANG, CustomerRank.KIM_CUONG).contains(rank)) {
			throw new RuntimeException("Loại khách hàng không hợp lệ: " + rank);
		}

		// 4️⃣ Chuẩn bị danh sách productIds, loại bỏ null
		List<Integer> productIds = request.getSanPhamIds() == null ? new ArrayList<>()
				: request.getSanPhamIds().stream().filter(Objects::nonNull).collect(Collectors.toList());

		// 5️⃣ Xóa chi tiết cũ để tránh lỗi orphanRemoval
		promotion.getChiTietKhuyenMais().clear();
		promotionDetailRepository.flush();

		// 6️⃣ Tạo chi tiết mới
		List<PromotionDetail> newDetails = new ArrayList<>();

		if (productIds.isEmpty()) {
			// Không chọn sản phẩm → áp dụng toàn shop
			PromotionDetail detail = new PromotionDetail();
			detail.setKhuyenMai(promotion);
			detail.setSanPham(null); // áp dụng tất cả sản phẩm
			detail.setLoaiKhachHang(rank); // giữ đúng rank từ request (có thể null)
			newDetails.add(detail);
		} else {
			// Tạo chi tiết cho từng sản phẩm
			for (Integer productId : productIds) {
				Product product = productRepository.findById(productId)
						.orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + productId));
				PromotionDetail detail = new PromotionDetail();
				detail.setKhuyenMai(promotion);
				detail.setSanPham(product);
				detail.setLoaiKhachHang(rank); // giữ đúng rank từ request
				newDetails.add(detail);
			}
		}

		// 7️⃣ Thêm chi tiết mới vào promotion
		promotion.getChiTietKhuyenMais().addAll(newDetails);

		// 8️⃣ Lưu và trả về DTO
		Promotion saved = promotionRepository.save(promotion);
		return PromotionMapping.toPromotionResponseDetail(saved);
	}

	@Override
	public void deletePromotion(Integer id) {
		if (!promotionRepository.existsById(id)) {
			throw new RuntimeException("Khuyến mãi không tồn tại!");
		}
		promotionRepository.deleteById(id);
	}

	@Override
	public PromotionResponse getPromotionById(Integer id) {
		Promotion promotion = promotionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi ID = " + id));
		return PromotionMapping.toPromotionResponseDetail(promotion);
	}

	@Override
	public List<PromotionResponse> getAllPromotions() {
		return promotionRepository.findAll().stream().map(PromotionMapping::toPromotionResponse)
				.collect(Collectors.toList());
	}

	// ----------------- SEARCH & FILTER -----------------
	@Override
	public List<PromotionResponse> searchByName(String keyword) {
		return promotionRepository.findByTenkmContainingIgnoreCase(keyword).stream()
				.map(PromotionMapping::toPromotionResponse).collect(Collectors.toList());
	}

	@Override
	public List<PromotionResponse> filterByStatus(PromotionStatus status) {
		return promotionRepository.findByTrangThai(status).stream().map(PromotionMapping::toPromotionResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PromotionResponse> filterByType(PromotionType type) {
		return promotionRepository.findByLoaiKm(type).stream().map(PromotionMapping::toPromotionResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PromotionResponse> filterByDateRange(LocalDateTime start, LocalDateTime end) {
		return promotionRepository.findByThoiGianBdBetween(start, end).stream()
				.map(PromotionMapping::toPromotionResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PromotionResponse> searchPromotions(String keyword, PromotionType type, PromotionStatus status,
			Pageable pageable) {
		return promotionRepository.searchPromotions(keyword, type, status, pageable)
				.map(PromotionMapping::toPromotionResponse);
	}

	@Override
	@Transactional
	public Promotion updateStatus(Integer id, PromotionStatus newStatus) {
	    Promotion promo = promotionRepository.findById(id)
	            .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khuyến mãi với id: " + id));

	    LocalDateTime now = LocalDateTime.now();

	    // Nếu đã hết hạn thì luôn set EXPIRED
	    if (promo.getThoiGianKt() != null && promo.getThoiGianKt().isBefore(now)) {
	        promo.setTrangThai(PromotionStatus.EXPIRED);
	    } else {
	        // Ngược lại thì set trạng thái mới
	        promo.setTrangThai(newStatus);
	    }

	    return promotionRepository.save(promo);
	}


	@Override
	@Transactional(readOnly = true)
	public PromotionResponse getPromotionDetail(Integer id) {
		Optional<Promotion> promotionOpt = promotionRepository.findById(id);
		if (promotionOpt.isEmpty()) {
			return null; // hoặc ném exception
		}

		Promotion promotion = promotionOpt.get();
		// Map sang DTO có chi tiết + sản phẩm
		return PromotionMapping.toPromotionResponseDetail(promotion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PromotionResponse> findByProductMaSP(Integer maSP) {
		return promotionDetailRepository.findBySanPhamMaSP(maSP).stream().map(PromotionDetail::getKhuyenMai) // lấy
																												// Promotion
																												// từ
																												// PromotionDetail
				.distinct().map(PromotionMapping::toPromotionResponse).collect(Collectors.toList());
	}
}
