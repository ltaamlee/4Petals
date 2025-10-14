package fourpetals.com.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "SanPhamNguyenLieu")
public class ProductMaterial {

	@EmbeddedId
	private ProductMaterialId id = new ProductMaterialId();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MaSP")
	@MapsId("maSP") 
	private Product maSP;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MaNL")
	@MapsId("maNL") 
	private Material maNL;

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

	
	
	public ProductMaterial() {
		super();
	}

	public ProductMaterial(ProductMaterialId id, Product maSP, Material maNL) {
		super();
		this.id = id;
		this.maSP = maSP;
		this.maNL = maNL;
	}
	
	

}
