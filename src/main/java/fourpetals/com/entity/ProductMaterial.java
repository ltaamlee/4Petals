package fourpetals.com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SanPhamNguyenLieu")
public class ProductMaterial {

	@EmbeddedId
	private ProductMaterialId id = new ProductMaterialId();

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productId")
	@JoinColumn(name = "MaSP")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("materialId")
	@JoinColumn(name = "MaNL")
	private Material material;

	@Column(name = "SoLuong", nullable = false)
	private Integer soLuong = 1;

	public ProductMaterial() {
	}

	public ProductMaterial(Product p, Material m, Integer qty) {
		setProduct(p);
		setMaterial(m);
		setSoLuong(qty);
		this.id = new ProductMaterialId(p.getMaSP(), m.getMaNL());
	}

	public ProductMaterialId getId() {
		return id;
	}

	public void setId(ProductMaterialId id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}
}
