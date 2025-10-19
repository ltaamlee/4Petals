package fourpetals.com.entity;

import fourpetals.com.enums.CustomerRank;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ChiTietKhuyenMai")
public class PromotionDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "makm", nullable = false)
	private Promotion khuyenMai; // mã khuyến mãi

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "masp", nullable = true)
	private Product sanPham; // mã sản phẩm

	@Enumerated(EnumType.STRING)
	private CustomerRank loaiKhachHang;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Promotion getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(Promotion khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public Product getSanPham() {
		return sanPham;
	}

	public void setSanPham(Product sanPham) {
		this.sanPham = sanPham;
	}

	public CustomerRank getLoaiKhachHang() {
		return loaiKhachHang;
	}

	public void setLoaiKhachHang(CustomerRank loaiKhachHang) {
		this.loaiKhachHang = loaiKhachHang;
	}
	

}
