package fourpetals.com.controller.shipper;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.entity.Product;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    // ======= 1️⃣ HIỂN THỊ DANH SÁCH ĐƠN HÀNG =======
    @GetMapping("/orders")
    public String hienThiDanhSachDonHang(Model model) {
        // ---- Khách hàng mẫu ----
        Customer kh1 = new Customer();
        kh1.setMaKH(1);
        kh1.setHoTen("Nguyễn Văn A");

        Customer kh2 = new Customer();
        kh2.setMaKH(2);
        kh2.setHoTen("Trần Thị B");

        // ---- Sản phẩm mẫu ----
        Product sp1 = new Product();
        sp1.setGia(new BigDecimal("120000"));
        Product sp2 = new Product();
        sp2.setGia(new BigDecimal("150000"));
        Product sp3 = new Product();
        sp3.setGia(new BigDecimal("80000"));

        // ---- Đơn hàng mẫu ----
        Order dh1 = new Order();
        dh1.setMaDH(1001);
        dh1.setKhachHang(kh1);
        dh1.setTongTien(sp1.getGia().add(sp2.getGia()));

        Order dh2 = new Order();
        dh2.setMaDH(1002);
        dh2.setKhachHang(kh2);
        dh2.setTongTien(sp3.getGia());

        List<Order> listOrders = List.of(dh1, dh2);
        model.addAttribute("listOrders", listOrders);

        return "shipper/main"; // Giao diện Thymeleaf
    }

    // ======= 2️⃣ XEM CHI TIẾT ĐƠN HÀNG =======
    @GetMapping("/{maDH}/details")
    @ResponseBody
    public Map<String, Object> getOrderDetails(@PathVariable Integer maDH) {
        Map<String, Object> detail = new HashMap<>();

        if (maDH == 1001) {
            detail.put("tenKhachHang", "Nguyễn Văn A");
            detail.put("diaChi", "123 Nguyễn Trãi, Hà Nội");
            detail.put("soDienThoai", "0905123456");
            detail.put("sanPham", String.join("\n", List.of("Sữa tắm 4Petals", "Dầu gội 4Petals")));
            detail.put("thanhTien", "290000");
        } else {
            detail.put("tenKhachHang", "Trần Thị B");
            detail.put("diaChi", "45 Lê Lợi, Đà Nẵng");
            detail.put("soDienThoai", "0987456123");
            detail.put("sanPham", "Pate cho mèo");
            detail.put("thanhTien", "80000");
        }

        return detail;
    }

    // ======= 3️⃣ TIẾP NHẬN ĐƠN HÀNG =======
    @PostMapping("/{maDH}/confirm")
    @ResponseBody
    public String confirmOrder(@PathVariable Integer maDH) {
        System.out.println("✅ Đơn hàng " + maDH + " đã được shipper tiếp nhận (test).");
        return "OK";
    }
}
