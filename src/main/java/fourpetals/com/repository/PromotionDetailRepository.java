package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
	
	  // Tìm tất cả PromotionDetail theo Promotion
    List<PromotionDetail> findByKhuyenMai(Promotion promotion);

    // Tìm tất cả PromotionDetail theo mã sản phẩm
    List<PromotionDetail> findBySanPhamMaSP(Integer maSP);


}
