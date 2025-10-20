package fourpetals.com.dto.response.orders;

import java.math.BigDecimal;

import fourpetals.com.entity.OrderDetail;

public class OrderItemResponse {
	private Integer maSP;
	private String tenSP;
	private Integer soLuong;
	private BigDecimal giaBan;

	public static OrderItemResponse fromEntity(OrderDetail detail) {
        if (detail == null) return null;

        OrderItemResponse dto = new OrderItemResponse();
        dto.setMaSP(detail.getSanPham() != null ? detail.getSanPham().getMaSP() : null);
        dto.setTenSP(detail.getSanPham() != null ? detail.getSanPham().getTenSP() : "N/A");
        dto.setSoLuong(detail.getSoLuong());
        dto.setGiaBan(detail.getGiaBan());
        return dto;
    }

	public Integer getMaSP() {
		return maSP;
	}

	public void setMaSP(Integer maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public BigDecimal getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(BigDecimal giaBan) {
		this.giaBan = giaBan;
	}
	

}
