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
}