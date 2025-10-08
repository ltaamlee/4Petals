package fourpetals.com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NguyenLieu")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Material {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaNL")
	private Integer maNL;

	@Column(name = "TenNL", length = 100, nullable = false)
	private String tenNL;

	@Column(name = "SoLuongTon")
	private Integer soLuongTon;

	@Column(name = "DonViTinh", length = 50)
	private String donViTinh;

	@ManyToOne
	@JoinColumn(name = "MaNCC", nullable = false)
	private Supplier nhaCungCap;

	public Integer getMaNL() {
		return maNL;
	}

	public void setMaNL(Integer maNL) {
		this.maNL = maNL;
	}

	public String getTenNL() {
		return tenNL;
	}

	public void setTenNL(String tenNL) {
		this.tenNL = tenNL;
	}

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public Supplier getNhaCungCap() {
		return nhaCungCap;
	}

	public void setNhaCungCap(Supplier nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}

}
