package fourpetals.com.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fourpetals.com.dto.response.promotions.PromotionResponse;
import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.PromotionDetail;
import fourpetals.com.enums.CustomerRank;

public final class PromotionMapping {

	private PromotionMapping() {
	}

	// Map thông tin cơ bản + CustomerRank duy nhất
	public static PromotionResponse toPromotionResponse(Promotion promotion) {
		if (promotion == null)
			return null;

		PromotionResponse dto = new PromotionResponse();
		dto.setMakm(promotion.getMakm());
		dto.setTenkm(promotion.getTenkm());
		dto.setLoaiKm(promotion.getLoaiKm());
		dto.setTrangThai(promotion.getTrangThai());
		dto.setGiaTri(promotion.getGiaTri());
		dto.setThoiGianBd(promotion.getThoiGianBd());
		dto.setThoiGianKt(promotion.getThoiGianKt());
		dto.setMoTa(promotion.getMoTa());

		// Lấy CustomerRank từ chi tiết đầu tiên (mỗi Promotion chỉ có 1 rank)
		if (promotion.getChiTietKhuyenMais() != null && !promotion.getChiTietKhuyenMais().isEmpty()) {
			PromotionDetail firstDetail = promotion.getChiTietKhuyenMais().get(0);
			dto.setLoaiKhachHang(firstDetail.getLoaiKhachHang());
		}

		return dto;
	}

	// Map chi tiết + danh sách sản phẩm (có thể null)
	public static PromotionResponse toPromotionResponseDetail(Promotion promotion) {
	    if (promotion == null)
	        return null;

	    PromotionResponse dto = toPromotionResponse(promotion);

	    List<PromotionDetail> details = promotion.getChiTietKhuyenMais();
	    if (details != null && !details.isEmpty()) {
	        dto.setSanPhamIds(details.stream()
	                .map(pd -> pd.getSanPham() != null ? pd.getSanPham().getMaSP() : null)
	                .collect(Collectors.toList()));

	        dto.setSanPhamNames(details.stream()
	                .map(pd -> pd.getSanPham() != null ? pd.getSanPham().getTenSP() : null)
	                .collect(Collectors.toList()));
	    } else {
	        // Nếu không có chi tiết, trả về list rỗng thay vì null
	        dto.setSanPhamIds(Collections.emptyList());
	        dto.setSanPhamNames(Collections.emptyList());
	    }

	    return dto;
	}

}
