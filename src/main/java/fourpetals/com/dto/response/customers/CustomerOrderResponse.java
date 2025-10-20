package fourpetals.com.dto.response.customers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import fourpetals.com.enums.OrderStatus;

public class CustomerOrderResponse {
    private Integer maDH;
    private LocalDateTime ngayDat;
    private BigDecimal tongTien;
    private OrderStatus trangThai;
    private List<OrderItemDTO> chiTiet;

    // Constructor
    public CustomerOrderResponse(Integer maDH, LocalDateTime ngayDat, BigDecimal tongTien, 
                         OrderStatus trangThai, List<OrderItemDTO> chiTiet) {
        this.maDH = maDH;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.chiTiet = chiTiet;
    }

    // Getter
    public Integer getMaDH() { return maDH; }
    public LocalDateTime getNgayDat() { return ngayDat; }
    public BigDecimal getTongTien() { return tongTien; }
    public OrderStatus getTrangThai() { return trangThai; }
    public List<OrderItemDTO> getChiTiet() { return chiTiet; }
}
