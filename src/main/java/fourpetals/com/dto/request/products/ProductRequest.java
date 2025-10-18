package fourpetals.com.dto.request.products;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.*;

public class ProductRequest {
	@NotBlank
	private String tenSP;
	private String donViTinh;
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal gia;
	private Integer soLuongTon;
	private String moTa;
	private String hinhAnh; // BE sẽ set sau khi upload
	@NotNull
	private Integer danhMucId;

	@NotNull
	private List<ProductMaterialLineRequest> materials;

	// getters/setters
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

	public Integer getDanhMucId() {
		return danhMucId;
	}

	public void setDanhMucId(Integer danhMucId) {
		this.danhMucId = danhMucId;
	}

	public List<ProductMaterialLineRequest> getMaterials() {
		return materials;
	}

	public void setMaterials(List<ProductMaterialLineRequest> materials) {
		this.materials = materials;
	}
}
