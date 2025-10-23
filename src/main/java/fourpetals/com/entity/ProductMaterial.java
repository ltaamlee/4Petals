package fourpetals.com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SanPhamNguyenLieu")
public class ProductMaterial {

	@EmbeddedId
	private ProductMaterialId id = new ProductMaterialId();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MaSP")
	@MapsId("maSP")
	private Product maSP;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MaNL")
	@MapsId("maNL")
	private Material maNL;

	@Column(name = "so_luong_can", nullable = false)
	private Integer soLuongCan = 1;

	public ProductMaterialId getId() {
		return id;
	}

	public void setId(ProductMaterialId id) {
		this.id = id;
	}

	public Product getMaSP() {
		return maSP;
	}

	public void setMaSP(Product maSP) {
		this.maSP = maSP;
	}

	public Material getMaNL() {
		return maNL;
	}

	public void setMaNL(Material maNL) {
		this.maNL = maNL;
	}

	public Integer getSoLuongCan() {
		return soLuongCan;
	}

	public void setSoLuongCan(Integer soLuongCan) {
		this.soLuongCan = soLuongCan;
	}

	public ProductMaterial() {
	}

	public ProductMaterial(ProductMaterialId id, Product maSP, Material maNL, Integer soLuongCan) {
		this.id = id;
		this.maSP = maSP;
		this.maNL = maNL;
		this.soLuongCan = soLuongCan;
	}
}