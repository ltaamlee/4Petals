package fourpetals.com.dto.response.customers;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String tenSanPham;
    private Integer soLuong;
    private BigDecimal giaBan;
    private String hinhAnh;

    // Constructor
    public OrderItemDTO(String tenSanPham, Integer soLuong, BigDecimal giaBan, String hinhAnh) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
    }

    // Getter
    public String getTenSanPham() { return tenSanPham; }
    public Integer getSoLuong() { return soLuong; }
    public BigDecimal getGiaBan() { return giaBan; }
    public String getHinhAnh() { return hinhAnh; }
}
