package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="KhuyenMai")
public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer makm;

	@Column(name = "tenkm", nullable = false, columnDefinition = "nvarchar(150)")
	private String tenkm;

	@Column(name = "loai_km", columnDefinition = "nvarchar(100)")
	@Enumerated(EnumType.STRING)
	private PromotionType loaiKm; // PERCENT, AMOUNT

	@Column(name = "trang_thai", length = 15)
	@Enumerated(EnumType.STRING)
	private PromotionStatus trangThai; 

	@Column(name = "gia_tri", nullable = false, precision = 9, scale = 2)
	private BigDecimal giaTri;

	@Column(name = "thoigian_bd", nullable = false)
	private LocalDateTime thoiGianBd;

	@Column(name = "thoigian_kt", nullable = false)
	private LocalDateTime thoiGianKt;


	@Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
	private String moTa;

	// Quan hệ 1-n với ChiTietKhuyenMai
	@OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PromotionDetail> chiTietKhuyenMais;

	public Integer getMakm() {
		return makm;
	}

	public void setMakm(Integer makm) {
		this.makm = makm;
	}

	public String getTenkm() {
		return tenkm;
	}

	public void setTenkm(String tenkm) {
		this.tenkm = tenkm;
	}

	public PromotionType getLoaiKm() {
		return loaiKm;
	}

	public void setLoaiKm(PromotionType loaiKm) {
		this.loaiKm = loaiKm;
	}

	public PromotionStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(PromotionStatus trangThai) {
		this.trangThai = trangThai;
	}

	public BigDecimal getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(BigDecimal giaTri) {
		this.giaTri = giaTri;
	}

	public LocalDateTime getThoiGianBd() {
		return thoiGianBd;
	}

	public void setThoiGianBd(LocalDateTime thoiGianBd) {
		this.thoiGianBd = thoiGianBd;
	}

	public LocalDateTime getThoiGianKt() {
		return thoiGianKt;
	}

	public void setThoiGianKt(LocalDateTime thoiGianKt) {
		this.thoiGianKt = thoiGianKt;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public List<PromotionDetail> getChiTietKhuyenMais() {
		return chiTietKhuyenMais;
	}

	public void setChiTietKhuyenMais(List<PromotionDetail> chiTietKhuyenMais) {
		this.chiTietKhuyenMais = chiTietKhuyenMais;
	}

	
	
	public Promotion() {
		super();
	}

	public Promotion(Integer makm, String tenkm, PromotionType loaiKm, PromotionStatus trangThai, BigDecimal giaTri,
			LocalDateTime thoiGianBd, LocalDateTime thoiGianKt, String moTa, List<PromotionDetail> chiTietKhuyenMais) {
		super();
		this.makm = makm;
		this.tenkm = tenkm;
		this.loaiKm = loaiKm;
		this.trangThai = trangThai;
		this.giaTri = giaTri;
		this.thoiGianBd = thoiGianBd;
		this.thoiGianKt = thoiGianKt;
		this.moTa = moTa;
		this.chiTietKhuyenMais = chiTietKhuyenMais;
	}
	
	
}
