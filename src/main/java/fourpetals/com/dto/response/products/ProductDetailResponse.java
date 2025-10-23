package fourpetals.com.dto.response.products;

import java.math.BigDecimal;
import java.util.List;

import fourpetals.com.enums.CustomerRank;

public class ProductDetailResponse {
	private Integer maSP;
	private String tenSP;
	private String donViTinh;
	private BigDecimal gia;
	private Integer soLuongTon;
	private String moTa;
	private String hinhAnh;
	private Integer trangThai;
	private String trangThaiText;
	private Integer maDM;
	private List<MaterialLine> materials;

	private String bannerKhuyenMai;
	private BigDecimal giaSauKhuyenMai;

	private String loaiKhuyenMai; // "SALE" hoặc "GIFT"
	private Integer giamPhanTram;
	private CustomerRank customerRank;

	
	public CustomerRank getCustomerRank() {
		return customerRank;
	}

	public void setCustomerRank(CustomerRank customerRank) {
		this.customerRank = customerRank;
	}

	public Integer getGiamPhanTram() {
		return giamPhanTram;
	}

	public void setGiamPhanTram(Integer giamPhanTram) {
		this.giamPhanTram = giamPhanTram;
	}

	public String getLoaiKhuyenMai() {
		return loaiKhuyenMai;
	}

	public void setLoaiKhuyenMai(String loaiKhuyenMai) {
		this.loaiKhuyenMai = loaiKhuyenMai;
	}

	public String getBannerKhuyenMai() {
		return bannerKhuyenMai;
	}

	public void setBannerKhuyenMai(String bannerKhuyenMai) {
		this.bannerKhuyenMai = bannerKhuyenMai;
	}

	public BigDecimal getGiaSauKhuyenMai() {
		return giaSauKhuyenMai;
	}

	public void setGiaSauKhuyenMai(BigDecimal giaSauKhuyenMai) {
		this.giaSauKhuyenMai = giaSauKhuyenMai;
	}

	public ProductDetailResponse(Integer maSP, String tenSP, String donViTinh, BigDecimal gia, Integer soLuongTon,
			String moTa, String hinhAnh, Integer trangThai, String trangThaiText, Integer maDM,
			List<MaterialLine> materials, String bannerKhuyenMai, BigDecimal giaSauKhuyenMai, String loaiKhuyenMai,
			Integer giamPhanTram) {
		super();
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.donViTinh = donViTinh;
		this.gia = gia;
		this.soLuongTon = soLuongTon;
		this.moTa = moTa;
		this.hinhAnh = hinhAnh;
		this.trangThai = trangThai;
		this.trangThaiText = trangThaiText;
		this.maDM = maDM;
		this.materials = materials;
		this.bannerKhuyenMai = bannerKhuyenMai;
		this.giaSauKhuyenMai = giaSauKhuyenMai;
		this.loaiKhuyenMai = loaiKhuyenMai;
		this.giamPhanTram = giamPhanTram;
	}

	// --- Constructors ---
	public ProductDetailResponse() {
	}

	public ProductDetailResponse(Integer maSP, String tenSP, String donViTinh, BigDecimal gia, Integer soLuongTon,
			String moTa, String hinhAnh, Integer trangThai, String trangThaiText, Integer maDM,
			List<MaterialLine> materials) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.donViTinh = donViTinh;
		this.gia = gia;
		this.soLuongTon = soLuongTon;
		this.moTa = moTa;
		this.hinhAnh = hinhAnh;
		this.trangThai = trangThai;
		this.trangThaiText = trangThaiText;
		this.maDM = maDM;
		this.materials = materials;
	}

	// --- Getters/Setters ---
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

	public String getTrangThaiText() {
		return trangThaiText;
	}

	public void setTrangThaiText(String trangThaiText) {
		this.trangThaiText = trangThaiText;
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

	// --- Inner class ---
	public static class MaterialLine {
		private Integer maNL;
		private String tenNL;
		private String donViTinh;
		private Integer soLuongCan;

		public MaterialLine() {
		}

		public MaterialLine(Integer maNL, String tenNL, String donViTinh, Integer soLuongCan) {
			this.maNL = maNL;
			this.tenNL = tenNL;
			this.donViTinh = donViTinh;
			this.soLuongCan = soLuongCan;
		}

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

		public Integer getSoLuongCan() {
			return soLuongCan;
		}

		public void setSoLuongCan(Integer soLuongCan) {
			this.soLuongCan = soLuongCan;
		}
	}

}