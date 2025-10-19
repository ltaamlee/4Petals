// file: fourpetals/com/entity/Product.java
package fourpetals.com.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import fourpetals.com.enums.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "SanPham")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaSP")
	private Integer maSP;

	@Column(name = "TenSP", nullable = false, columnDefinition = "nvarchar(100)")
	private String tenSP;

	@Column(name = "DonViTinh", columnDefinition = "nvarchar(50)")
	private String donViTinh;

	@Column(name = "Gia", precision = 18, scale = 2, nullable = false)
	private BigDecimal gia;

	@Column(name = "SoLuongTon")
	private Integer soLuongTon;

	@Column(name = "MoTa", columnDefinition = "nvarchar(50)")
	private String moTa;

	@Column(name = "HinhAnh", length = 255)
	private String hinhAnh;

	@Column(name = "TrangThai", columnDefinition = "nvarchar(50)")
	private Integer trangThai = ProductStatus.DANG_BAN.getValue();

	@Column(name = "LuotXem")
	private Integer luotXem = 0;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MaDM")
	private Category danhMuc;

	// ★ Quan hệ ngược để load "Bao gồm"
	@OneToMany(mappedBy = "maSP", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductMaterial> productMaterials = new HashSet<>();

	// ====== ENUM helpers ======
	@Transient
	public ProductStatus getProductStatus() {
		return ProductStatus.fromValue(this.trangThai);
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.trangThai = productStatus.getValue();
	}

	public void updateStatusBasedOnStock() {
		if (this.soLuongTon != null && this.soLuongTon <= 0) {
			this.setProductStatus(ProductStatus.HET_HANG);
		} else if (this.getProductStatus() == ProductStatus.HET_HANG && this.soLuongTon != null
				&& this.soLuongTon > 0) {
			this.setProductStatus(ProductStatus.DANG_BAN);
		}
	}
    @Transient
    public ProductStatus getTrangThaiEnum() {
        return ProductStatus.fromValue(this.trangThai);
    }

    @Transient
    public String getTrangThaiText() {
        return getTrangThaiEnum().getDisplayName();
    }

    public void setTrangThaiEnum(ProductStatus status) {
        this.trangThai = (status == null) ? null : status.getValue();
    }
	

	// getters/setters giữ nguyên tên
	public Integer getMaSP() {
		return maSP;
	}

	public void setMaSP(Integer maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public BigDecimal getGia() {
		return gia;
	}

	public void setGia(BigDecimal gia) {
		this.gia = gia;
	}

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Integer getLuotXem() {
		return luotXem;
	}

	public void setLuotXem(Integer luotXem) {
		this.luotXem = luotXem;
	}

	public Category getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(Category danhMuc) {
		this.danhMuc = danhMuc;
	}

	public Set<ProductMaterial> getProductMaterials() {
		return productMaterials;
	}

	public void setProductMaterials(Set<ProductMaterial> productMaterials) {
		this.productMaterials = productMaterials;
	}
}