package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;
import fourpetals.com.enums.CustomerRank;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {

	// Lấy tất cả PromotionDetail theo Promotion
	List<PromotionDetail> findByKhuyenMai(Promotion promotion);

	// Lấy tất cả PromotionDetail theo mã sản phẩm
	List<PromotionDetail> findBySanPhamMaSP(Integer maSP);

	// Lấy tất cả PromotionDetail ACTIVE áp dụng cho product + rank, eager fetch
	// khuyenMai để tránh Lazy
	@EntityGraph(attributePaths = { "khuyenMai", "sanPham" })
	@Query("""
			    SELECT pd FROM PromotionDetail pd
			    JOIN FETCH pd.khuyenMai km
			    WHERE km.trangThai = 'ACTIVE'
			      AND (km.thoiGianBd IS NULL OR km.thoiGianBd <= CURRENT_TIMESTAMP)
			      AND (km.thoiGianKt IS NULL OR km.thoiGianKt >= CURRENT_TIMESTAMP)
			      AND (pd.sanPham.id = :productId OR pd.sanPham IS NULL)
			      AND (:rank IS NULL OR pd.loaiKhachHang = :rank OR pd.loaiKhachHang IS NULL)
			""")
	List<PromotionDetail> findActivePromotionsByProduct(@Param("productId") Integer productId,
			@Param("rank") CustomerRank rank);

	// Cũ, giữ nguyên nếu cần
	@Query("SELECT d FROM PromotionDetail d WHERE d.khuyenMai.makm = :makm")
	List<PromotionDetail> findByKhuyenMai(@Param("makm") Integer makm);

}
