package fourpetals.com.dto.request.supplier;

import java.util.List;

import fourpetals.com.entity.SupplierMaterial;

public class SupplierRequest {
	private Integer maNCC;
    private String tenNCC;
    private String diaChi;
    private String sdt;
    private String email;
    private List<Integer> nhaCungCapNguyenLieu;
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
	public List<Integer> getNhaCungCapNguyenLieu() {
		return nhaCungCapNguyenLieu;
	}
	public void setNhaCungCapNguyenLieu(List<Integer> nhaCungCapNguyenLieu) {
		this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
	}
		
	public SupplierRequest() {
		super();
	}
	public SupplierRequest(Integer maNCC, String tenNCC, String diaChi, String sdt, String email,
			List<Integer> nhaCungCapNguyenLieu) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.sdt = sdt;
		this.email = email;
		this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
	} 
}
