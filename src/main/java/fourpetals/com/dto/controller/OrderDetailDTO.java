package fourpetals.com.dto.controller;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private String tenSanPham;
    private Integer soLuong; 
    
    private List<MaterialDetailDTO> chiTietNguyenLieu; 

    public OrderDetailDTO(String tenSanPham, Integer soLuong) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.chiTietNguyenLieu = List.of(); 
    }

//	public OrderDetailDTO(String tenSanPham, Integer soLuong, List<MaterialDetailDTO> chiTietNguyenLieu) {
//		super();
//		this.tenSanPham = tenSanPham;
//		this.soLuong = soLuong;
//		this.chiTietNguyenLieu = chiTietNguyenLieu;
//	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public List<MaterialDetailDTO> getChiTietNguyenLieu() {
		return chiTietNguyenLieu;
	}

	public void setChiTietNguyenLieu(List<MaterialDetailDTO> chiTietNguyenLieu) {
		this.chiTietNguyenLieu = chiTietNguyenLieu;
	}
    
    
}