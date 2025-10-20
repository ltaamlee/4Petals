package fourpetals.com.dto.response.supplier;

import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.SupplierStatus;

public class SupplierResponse {
	private Integer maNCC;
	private String tenNCC;
	private String diaChi;
	private String sdt;
	private String email;
	private SupplierStatus trangThai;
	private String moTa;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private List<Integer> nhaCungCapNguyenLieu;
	private List<String> nhaCungCapNguyenLieuNames;

	
	
	public SupplierResponse() {
	}
	

	public SupplierResponse(Integer maNCC, String tenNCC, String diaChi, String sdt, String email,
			SupplierStatus trangThai, String moTa, LocalDateTime createdAt, LocalDateTime updatedAt,
			List<Integer> nhaCungCapNguyenLieu, List<String> nhaCungCapNguyenLieuNames) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.sdt = sdt;
		this.email = email;
		this.trangThai = trangThai;
		this.moTa = moTa;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
		this.nhaCungCapNguyenLieuNames = nhaCungCapNguyenLieuNames;
	}





	// ----- getters & setters -----
	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SupplierStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(SupplierStatus trangThai) {
		this.trangThai = trangThai;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Integer> getNhaCungCapNguyenLieu() {
		return nhaCungCapNguyenLieu;
	}

	public void setNhaCungCapNguyenLieu(List<Integer> nhaCungCapNguyenLieu) {
		this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
	}

	public List<String> getNhaCungCapNguyenLieuNames() {
		return nhaCungCapNguyenLieuNames;
	}

	public void setNhaCungCapNguyenLieuNames(List<String> nhaCungCapNguyenLieuNames) {
		this.nhaCungCapNguyenLieuNames = nhaCungCapNguyenLieuNames;
	}
}
