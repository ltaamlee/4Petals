package fourpetals.com.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.Promotion;

@Service
public class ProductBannerService {
	private final Map<Integer, String> productBannerCache = new ConcurrentHashMap<>();

    // Khi khuyến mãi được kích hoạt
    public void applyPromotionBanner(Product product, Promotion promotion) {
        String bannerText = "🎉 " + promotion.getTenkm() + " - Giảm " 
                + promotion.getGiaTri() + (promotion.getLoaiKm().name().equals("PERCENT") ? "%" : "₫");
        productBannerCache.put(product.getMaSP(), bannerText);
    }

    // Khi khuyến mãi bị hủy hoặc hết hiệu lực
    public void removePromotionBanner(Product product) {
        productBannerCache.remove(product.getMaSP());
    }

    // Lấy banner hiện tại của sản phẩm
    public String getBannerForProduct(Integer productId) {
        return productBannerCache.get(productId);
    }

    // Debug hoặc API dùng để test
    public Map<Integer, String> getAllBanners() {
        return productBannerCache;
    }
}
