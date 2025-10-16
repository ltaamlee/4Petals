package fourpetals.com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NguyenLieu")
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaNL")
	private Integer maNL;

	@Column(name = "TenNL", nullable = false, columnDefinition = "nvarchar(100)")
	private String tenNL;

	@Column(name = "DonViTinh", columnDefinition = "nvarchar(50)")
	private String donViTinh;

	@Column(name = "SoLuongTon", nullable = false)
	private Integer soLuongTon = 0;


	// Getter & Setter
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

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public Material() {
		super();
	}

	public Material(Integer maNL, String tenNL, String donViTinh, Integer soLuongTon) {
		super();
		this.maNL = maNL;
		this.tenNL = tenNL;
		this.donViTinh = donViTinh;
		this.soLuongTon = soLuongTon;
	}
}
