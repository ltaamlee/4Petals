package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PhieuNhap")
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaPN")
	private Integer maPN;

	@Column(name = "NgayNhap", nullable = false)
	private LocalDate ngayNhap = LocalDate.now();

	@Column(name = "TongTien", precision = 18, scale = 2)
	private BigDecimal tongTien;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MaNV")
	private Employee nhanVien;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MaNCC")
	private Supplier nhaCungCap;
	
	@OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryDetail> chiTietPhieuNhaps;


	public Integer getMaPN() {
		return maPN;
	}

	public void setMaPN(Integer maPN) {
		this.maPN = maPN;
	}

	public LocalDate getNgayNhap() {
		return ngayNhap;
	}

	public void setNgayNhap(LocalDate ngayNhap) {
		this.ngayNhap = ngayNhap;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	public Employee getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(Employee nhanVien) {
		this.nhanVien = nhanVien;
	}

	public Supplier getNhaCungCap() {
		return nhaCungCap;
	}

	public void setNhaCungCap(Supplier nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}
}
