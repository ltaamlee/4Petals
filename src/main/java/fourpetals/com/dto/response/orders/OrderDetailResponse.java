package fourpetals.com.dto.response.orders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import fourpetals.com.entity.Order;

public class OrderDetailResponse {
	private Integer maDH;
	private Integer maKH;
	private String hoTenKH;
	private LocalDateTime ngayDat;
	private LocalDate ngayGiao;
	private BigDecimal tongTien;
	private BigDecimal phiVanChuyen;
	private String phuongThucThanhToan;
	private String trangThai;
	private String trangThaiThanhToan;
	private String ghiChu;

	private Integer maNhanVien; // <-- thêm mã nhân viên
	private String nhanVienXuLy; // tên nhân viên
	private LocalDateTime ngayCapNhat;
	private String diaChiGiao;
	private String sdtNguoiNhan;
	private List<OrderItemResponse> chiTietDonHang;

	public static OrderDetailResponse fromEntity(Order order) {
		if (order == null)
			return null;

		OrderDetailResponse resp = new OrderDetailResponse();
		resp.setMaDH(order.getMaDH());
		resp.setMaKH(order.getKhachHang() != null ? order.getKhachHang().getMaKH() : null);
		resp.setHoTenKH(order.getKhachHang() != null ? order.getKhachHang().getHoTen() : "N/A");
		resp.setNgayDat(order.getNgayDat());
		resp.setNgayGiao(order.getNgayGiao());
		resp.setTongTien(order.getTongTien());
		resp.setPhiVanChuyen(order.getPhiVanChuyen());
		resp.setPhuongThucThanhToan(
				order.getPhuongThucThanhToan() != null ? order.getPhuongThucThanhToan().getDisplayName() : "N/A");
		resp.setTrangThai(order.getTrangThai() != null ? order.getTrangThai().getDisplayName() : "N/A");
		resp.setTrangThaiThanhToan(
				order.getTrangThaiThanhToan() != null ? order.getTrangThaiThanhToan().getDisplayName() : "N/A");
		resp.setGhiChu(order.getGhiChu());

		if (order.getNhanVien() != null) {
			resp.setMaNhanVien(order.getNhanVien().getMaNV());
			resp.setNhanVienXuLy(order.getNhanVien().getHoTen());
		} else {
			resp.setMaNhanVien(null);
			resp.setNhanVienXuLy("N/A");
		}

		resp.setNgayCapNhat(order.getNgayCapNhat());
		resp.setDiaChiGiao(order.getDiaChiGiao());
		resp.setSdtNguoiNhan(order.getSdtNguoiNhan());

		if (order.getChiTietDonHang() != null) {
			List<OrderItemResponse> items = order.getChiTietDonHang().stream().map(OrderItemResponse::fromEntity)
					.collect(Collectors.toList());
			resp.setChiTietDonHang(items);
		}

		return resp;
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

	public LocalDate getNgayGiao() {
		return ngayGiao;
	}

	public void setNgayGiao(LocalDate ngayGiao) {
		this.ngayGiao = ngayGiao;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	public BigDecimal getPhiVanChuyen() {
		return phiVanChuyen;
	}

	public void setPhiVanChuyen(BigDecimal phiVanChuyen) {
		this.phiVanChuyen = phiVanChuyen;
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

	public String getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void setTrangThaiThanhToan(String trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public Integer getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(Integer maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getNhanVienXuLy() {
		return nhanVienXuLy;
	}

	public void setNhanVienXuLy(String nhanVienXuLy) {
		this.nhanVienXuLy = nhanVienXuLy;
	}

	public LocalDateTime getNgayCapNhat() {
		return ngayCapNhat;
	}

	public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
		this.ngayCapNhat = ngayCapNhat;
	}

	public String getDiaChiGiao() {
		return diaChiGiao;
	}

	public void setDiaChiGiao(String diaChiGiao) {
		this.diaChiGiao = diaChiGiao;
	}

	public String getSdtNguoiNhan() {
		return sdtNguoiNhan;
	}

	public void setSdtNguoiNhan(String sdtNguoiNhan) {
		this.sdtNguoiNhan = sdtNguoiNhan;
	}

	public List<OrderItemResponse> getChiTietDonHang() {
		return chiTietDonHang;
	}

	public void setChiTietDonHang(List<OrderItemResponse> chiTietDonHang) {
		this.chiTietDonHang = chiTietDonHang;
	}

}
