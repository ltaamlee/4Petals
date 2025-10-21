package fourpetals.com.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import fourpetals.com.dto.request.promotions.PromotionCreateRequest;
import fourpetals.com.dto.request.promotions.PromotionUpdateRequest;
import fourpetals.com.dto.response.promotions.PromotionResponse;
import fourpetals.com.dto.response.stats.PromotionStatsResponse;
import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;

public interface PromotionService {

	// ------------------ C-R-U-D -------------------------------
	PromotionResponse createPromotion(PromotionCreateRequest request);

    PromotionResponse updatePromotion(Integer id, PromotionUpdateRequest request);

    void deletePromotion(Integer id);

    
    //Thống kê
    
    PromotionStatsResponse getPromotionStats(int daysToExpire);

    
    
	// ------------------ Tìm kiếm + lọc-------------------------------
    
    PromotionResponse getPromotionDetail(Integer id);

    PromotionResponse getPromotionById(Integer id);

    List<PromotionResponse> getAllPromotions();

    List<PromotionResponse> searchByName(String keyword);

    List<PromotionResponse> filterByStatus(PromotionStatus status);

    List<PromotionResponse> filterByType(PromotionType type);

    List<PromotionResponse> filterByDateRange(LocalDateTime start, LocalDateTime end);

    
    //Tìm kiếm và lọc nâng cao
    
    Page<PromotionResponse> searchPromotions(String keyword, PromotionType type, PromotionStatus status, Pageable pageable);

	Promotion updateStatus(Integer id, PromotionStatus trangThai);
    
	List<PromotionResponse> findByProductMaSP(Integer maSP);
	
	
	//KHUYẾN MÃI
    Optional<PromotionResponse> getActivePromotionForProduct(Integer productId, CustomerRank rank);

	String findActiveBannerForProduct(Integer maSP);

}
