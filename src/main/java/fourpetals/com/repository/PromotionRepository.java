package fourpetals.com.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

	// Đếm khuyến mãi theo trạng thái enum
	long countByTrangThai(PromotionStatus status);

	// Đếm khuyến mãi active trong khoảng thời gian hiện tại
	long countByTrangThaiAndThoiGianBdBeforeAndThoiGianKtAfter(PromotionStatus status, LocalDateTime now1,
			LocalDateTime now2);

	List<Promotion> findByTrangThaiAndThoiGianKtBefore(PromotionStatus status, LocalDateTime now);

	// Đếm khuyến mãi active sắp hết hạn
	long countByTrangThaiAndThoiGianKtBetween(PromotionStatus status, LocalDateTime start, LocalDateTime end);

	long countByTrangThaiAndThoiGianKtLessThanEqual(PromotionStatus status, LocalDateTime endDate);

	
	// Tìm kiếm
	// 🔍 Tìm kiếm theo tên (chứa từ khóa)
	List<Promotion> findByTenkmContainingIgnoreCase(String keyword);

	// 🔍 Lọc theo trạng thái
	List<Promotion> findByTrangThai(PromotionStatus status);

	// 🔍 Lọc theo loại khuyến mãi
	List<Promotion> findByLoaiKm(PromotionType type);

	// 🔍 Lọc theo thời gian bắt đầu và kết thúc
	List<Promotion> findByThoiGianBdBetween(LocalDateTime start, LocalDateTime end);

	// 🔍 Tìm kiếm phân trang theo tên
	Page<Promotion> findByTenkmContainingIgnoreCase(String keyword, Pageable pageable);

	// 🔍 Lọc nâng cao phân trang (tên + loại + trạng thái)
	@EntityGraph(attributePaths = { "chiTietKhuyenMais", "chiTietKhuyenMais.sanPham" })
	@Query("SELECT p FROM Promotion p WHERE "
			+ "(:keyword IS NULL OR LOWER(p.tenkm) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND "
			+ "(:type IS NULL OR p.loaiKm = :type) AND " + "(:status IS NULL OR p.trangThai = :status)")
	Page<Promotion> searchPromotions(@Param("keyword") String keyword, @Param("type") PromotionType type,
			@Param("status") PromotionStatus status, Pageable pageable);

	
	@EntityGraph(attributePaths = { "chiTietKhuyenMais", "chiTietKhuyenMais.sanPham" })
	Optional<Promotion> findById(Integer id);

}
