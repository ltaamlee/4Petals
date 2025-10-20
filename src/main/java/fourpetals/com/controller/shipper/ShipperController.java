package fourpetals.com.controller.shipper;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.OrderRepository;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    // HIỂN THỊ DANH SÁCH ĐƠN HÀNG
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> listOrders = orderRepository.findAllDeliveringOrders();
        model.addAttribute("listOrders", listOrders);

        return "shipper/main"; 
    }

    // XEM CHI TIẾT ĐƠN HÀNG 

    @Transactional
    @GetMapping("/{maDH}/details")
    @ResponseBody
    public Map<String, Object> getOrderDetails(@PathVariable("maDH") Integer maDH) {
        Map<String, Object> result = new HashMap<>();

        Order donHang = orderRepository.findById(maDH).orElse(null);
        if (donHang == null) {
            result.put("error", "Không tìm thấy đơn hàng");
            return result;
        }
        
        String tenKhachHang = donHang.getKhachHang() != null ? donHang.getKhachHang().getHoTen() : "";
        String diaChi = donHang.getDiaChiGiao() != null ? donHang.getDiaChiGiao() : "";
        String soDienThoai = donHang.getSdtNguoiNhan() != null ? donHang.getSdtNguoiNhan() : "";
        String thanhTien = donHang.getTongTien() != null ? donHang.getTongTien().toPlainString() : "0";

        String phuongThucThanhToan = donHang.getPhuongThucThanhToan() != null 
                                     ? donHang.getPhuongThucThanhToan().toString() 
                                     : "Chưa xác định";
        String ghiChu = donHang.getGhiChu() != null ? donHang.getGhiChu() : "";
        String sanPham = donHang.getChiTietDonHang().stream()
                .map(ct -> ct.getSanPham().getTenSP() + " (x" + ct.getSoLuong() + ")")
                .collect(Collectors.joining(", "));
                
        result.put("tenKhachHang", tenKhachHang);
        result.put("diaChi", diaChi);
        result.put("soDienThoai", soDienThoai);
        result.put("sanPham", sanPham);
        result.put("thanhTien", thanhTien);
        result.put("phuongThucThanhToan", phuongThucThanhToan);
        result.put("ghiChu", ghiChu);

        return result;
    }
// // ✅ 3. Cập nhật trạng thái đơn hàng sang "Hoàn tất" và lưu vào DB
//    @PostMapping("/{maDH}/confirm")
//    @ResponseBody
//    @Transactional
//    public String confirmDelivery(@PathVariable Integer maDH) {
//        return orderRepository.findById(maDH)
//                .map(order -> {
//                    order.setTrangThai(OrderStatus.HOAN_TAT);
//                    orderRepository.save(order);
//                    return "Đơn hàng đã cập nhật trạng thái";
//                })
//                .orElse("Không tìm thấy đơn hàng có mã " + maDH);
//    }
}
