package fourpetals.com.dto.response.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import fourpetals.com.entity.Order;

public class OrderResponse {
	private Integer maDH;
	private Integer maKH; // mã khách hàng
	private String hoTenKH; // họ tên khách (nếu cần hiển thị)
	private LocalDateTime ngayDat;
	private BigDecimal tongTien;
	private String phuongThucThanhToan;
	private String trangThai;

	public static OrderResponse fromEntity(Order order) {
		if (order == null)
			return null;

		return new OrderResponse(order.getMaDH(),
				(order.getKhachHang() != null) ? order.getKhachHang().getMaKH() : null,
				(order.getKhachHang() != null) ? order.getKhachHang().getHoTen() : "N/A", order.getNgayDat(),
				order.getTongTien(),
				(order.getPhuongThucThanhToan() != null) ? order.getPhuongThucThanhToan().getDisplayName() : "N/A",
				(order.getTrangThai() != null) ? order.getTrangThai().getDisplayName() : "N/A");
	}

	public OrderResponse() {
		super();
	}

	public OrderResponse(Integer maDH, Integer maKH, String hoTenKH, LocalDateTime ngayDat, BigDecimal tongTien,
			String phuongThucThanhToan, String trangThai) {
		super();
		this.maDH = maDH;
		this.maKH = maKH;
		this.hoTenKH = hoTenKH;
		this.ngayDat = ngayDat;
		this.tongTien = tongTien;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.trangThai = trangThai;
	}

	public Integer getMaDH() {
		return maDH;
	}

	public void setMaDH(Integer maDH) {
		this.maDH = maDH;
	}

	public Integer getMaKH() {
		return maKH;
	}

	public void setMaKH(Integer maKH) {
		this.maKH = maKH;
	}

	public String getHoTenKH() {
		return hoTenKH;
	}

	public void setHoTenKH(String hoTenKH) {
		this.hoTenKH = hoTenKH;
	}

	public LocalDateTime getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDateTime ngayDat) {
		this.ngayDat = ngayDat;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}

	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

}
