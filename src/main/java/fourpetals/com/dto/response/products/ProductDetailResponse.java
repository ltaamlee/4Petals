package fourpetals.com.dto.response.products;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailResponse {
	private Integer maSP;
	private String tenSP;
	private String donViTinh;
	private BigDecimal gia;
	private Integer soLuongTon;
	private String moTa;
	private String hinhAnh;
	private Integer trangThai;
	private Integer maDM;
	private List<MaterialLine> materials;

	public static class MaterialLine {
		public Integer maNL;
		public String tenNL;
		public String donViTinh;
		public Integer soLuongCan;

		public MaterialLine(Integer maNL, String tenNL, String donViTinh, Integer soLuongCan) {
			this.maNL = maNL;
			this.tenNL = tenNL;
			this.donViTinh = donViTinh;
			this.soLuongCan = soLuongCan;
		}
	}

	// getters/setters
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

	public Integer getMaDM() {
		return maDM;
	}

	public void setMaDM(Integer maDM) {
		this.maDM = maDM;
	}

	public List<MaterialLine> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MaterialLine> materials) {
		this.materials = materials;
	}
}
