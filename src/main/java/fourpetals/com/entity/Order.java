package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.CancelRequestStatus;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaDH")
	private Integer maDH;

	@Column(name = "NgayDat", nullable = false)
	private LocalDateTime ngayDat = LocalDateTime.now();

	@Column(name = "NgayGiao")
	private LocalDate ngayGiao;

	@Enumerated(EnumType.STRING)
	@Column(name = "TrangThai", columnDefinition = "nvarchar(20)")
	private OrderStatus trangThai = OrderStatus.CHO_XU_LY;

	@Column(name = "DiaChiGiao", columnDefinition = "nvarchar(max)")
	private String diaChiGiao;

	@Column(name = "SDTNguoiNhan", length = 15)
	private String sdtNguoiNhan;

	@Column(name = "TongTien", precision = 18, scale = 2)
	private BigDecimal tongTien;

	@Column(name = "PhiVanChuyen", precision = 18, scale = 2)
	private BigDecimal phiVanChuyen = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@Column(name = "PhuongThucThanhToan", columnDefinition = "nvarchar(50)")
	private PaymentMethod phuongThucThanhToan;

	@Enumerated(EnumType.STRING)
	@Column(name = "TrangThaiThanhToan", columnDefinition = "nvarchar(20)")
	private PaymentStatus trangThaiThanhToan = PaymentStatus.CHUA_THANH_TOAN;

	@Column(name = "GhiChu", columnDefinition = "nvarchar(MAX)")
	private String ghiChu;

	@ManyToOne
	@JoinColumn(name = "MaKH")
	private Customer khachHang;

	@ManyToOne
	@JoinColumn(name = "MaNV_Duyet")
	private Employee nhanVienDuyet;

	@ManyToOne
	@JoinColumn(name = "MaNV_DongGoi")
	private Employee nhanVienDongGoi;

	@ManyToOne
	@JoinColumn(name = "MaNV_GiaoHang")
	private Employee nhanVienGiaoHang;

	@Column(name = "NgayCapNhat")
	private LocalDateTime ngayCapNhat;

	@Enumerated(EnumType.STRING)
	@Column(name = "TrangThaiHuyDon", columnDefinition = "nvarchar(50)")
	private CancelRequestStatus cancelRequestStatus = CancelRequestStatus.NONE;

	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetail> chiTietDonHang;

	public Integer getMaDH() {
		return maDH;
	}

	public void setMaDH(Integer maDH) {
		this.maDH = maDH;
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

	public OrderStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(OrderStatus trangThai) {
		this.trangThai = trangThai;
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

	public PaymentMethod getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}

	public void setPhuongThucThanhToan(PaymentMethod phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}

	public PaymentStatus getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void setTrangThaiThanhToan(PaymentStatus trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public Customer getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(Customer khachHang) {
		this.khachHang = khachHang;
	}

	@PreUpdate
	public void preUpdate() {
		this.ngayCapNhat = LocalDateTime.now();
	}

	public LocalDateTime getNgayCapNhat() {
		return ngayCapNhat;
	}

	public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
		this.ngayCapNhat = ngayCapNhat;
	}

	public List<OrderDetail> getChiTietDonHang() {
		return chiTietDonHang;
	}

	public void setChiTietDonHang(List<OrderDetail> chiTietDonHang) {
		this.chiTietDonHang = chiTietDonHang;
	}

	public CancelRequestStatus getCancelRequestStatus() {
		return cancelRequestStatus;
	}

	public void setCancelRequestStatus(CancelRequestStatus cancelRequestStatus) {
		this.cancelRequestStatus = cancelRequestStatus;
	}

	public Employee getNhanVienDuyet() {
		return nhanVienDuyet;
	}

	public void setNhanVienDuyet(Employee nhanVienDuyet) {
		this.nhanVienDuyet = nhanVienDuyet;
	}

	public Employee getNhanVienDongGoi() {
		return nhanVienDongGoi;
	}

	public void setNhanVienDongGoi(Employee nhanVienDongGoi) {
		this.nhanVienDongGoi = nhanVienDongGoi;
	}

	public Employee getNhanVienGiaoHang() {
		return nhanVienGiaoHang;
	}

	public void setNhanVienGiaoHang(Employee nhanVienGiaoHang) {
		this.nhanVienGiaoHang = nhanVienGiaoHang;
	}

}