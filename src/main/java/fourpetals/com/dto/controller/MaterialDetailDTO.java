package fourpetals.com.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDetailDTO {
    private String tenNguyenLieu;
    private Integer soLuongCan; // Số lượng nguyên liệu cần cho 1 sản phẩm
    private String donViTinh;   // Đơn vị tính của nguyên liệu
    private Integer tongSoLuongCan; // Tổng số lượng nguyên liệu cho số lượng sản phẩm đặt (Mới)
	
	public String getTenNguyenLieu() {
		return tenNguyenLieu;
	}
	public void setTenNguyenLieu(String tenNguyenLieu) {
		this.tenNguyenLieu = tenNguyenLieu;
	}
	public Integer getSoLuongCan() {
		return soLuongCan;
	}
	public void setSoLuongCan(Integer soLuongCan) {
		this.soLuongCan = soLuongCan;
	}
	public String getDonViTinh() {
		return donViTinh;
	}
	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}
	public Integer getTongSoLuongCan() {
		return tongSoLuongCan;
	}
	public void setTongSoLuongCan(Integer tongSoLuongCan) {
		this.tongSoLuongCan = tongSoLuongCan;
	}
//	public MaterialDetailDTO(String tenNguyenLieu, Integer soLuongCan, String donViTinh, Integer tongSoLuongCan) {
//		super();
//		this.tenNguyenLieu = tenNguyenLieu;
//		this.soLuongCan = soLuongCan;
//		this.donViTinh = donViTinh;
//		this.tongSoLuongCan = tongSoLuongCan;
//	}
//	public MaterialDetailDTO() {
//		super();
//	}
    
    
    
}