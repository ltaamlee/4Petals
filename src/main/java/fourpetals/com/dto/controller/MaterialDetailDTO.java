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
}