package fourpetals.com.controller.shipper;

import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;

@Controller
@RequestMapping("/shipper")
public class ProcessController {

    // ======= 1️⃣ HIỂN THỊ DANH SÁCH ĐƠN HÀNG =======
    @GetMapping("/process")
    public String hienThiDanhSachDonHangDangXuLy(Model model) {
        // ---- Khách hàng mẫu ----
        Customer kh1 = new Customer();
        kh1.setMaKH(1);
        kh1.setHoTen("Nguyễn Văn A");

        Customer kh2 = new Customer();
        kh2.setMaKH(2);
        kh2.setHoTen("Trần Thị B");

        Customer kh3 = new Customer();
        kh3.setMaKH(3);
        kh3.setHoTen("Phạm Văn C");

        // ---- Đơn hàng mẫu ----
        Order dh1 = new Order();
        dh1.setMaDH(1001);
        dh1.setKhachHang(kh1);
       // dh1.setTrangThai("dang_giao");

        Order dh2 = new Order();
        dh2.setMaDH(1002);
        dh2.setKhachHang(kh2);
       // dh2.setTrangThai("dang_giao");

        Order dh3 = new Order();
        dh3.setMaDH(1003);
        dh3.setKhachHang(kh3);
       // dh3.setTrangThai("dang_giao");

        // ---- Danh sách đơn hàng mẫu ----
        List<Order> listOrders = List.of(dh1, dh2, dh3);
        model.addAttribute("listOrders", listOrders);

        // Trả về trang giao diện
        return "shipper/process";
    }

    // ======= 2️⃣ LƯU CẬP NHẬT TRẠNG THÁI & GHI CHÚ CHO TẤT CẢ =======
    @PostMapping("/updateOrders")
    public String capNhatTatCaDonHang(@RequestParam Map<String, String> paramMap, Model model) {

        List<String> thongBaoLoi = new ArrayList<>();
        List<String> thongBaoThanhCong = new ArrayList<>();

        // Lặp qua tất cả các tham số trong form
        for (String key : paramMap.keySet()) {
            if (key.startsWith("status-")) {
                // Lấy mã đơn hàng
                String maDHStr = key.replace("status-", "");
                Integer maDH = Integer.valueOf(maDHStr);

                String trangThai = paramMap.get(key);
                String ghiChu = paramMap.get("note-" + maDHStr);

                // Kiểm tra logic: nếu thất bại mà không có ghi chú => lỗi
                if ("that_bai".equals(trangThai) && (ghiChu == null || ghiChu.trim().isEmpty())) {
                    thongBaoLoi.add("❌ Đơn hàng " + maDH + " thất bại nhưng chưa có ghi chú.");
                } else {
                    // Giả lập cập nhật DB
                    System.out.println("✅ Cập nhật đơn hàng " + maDH);
                    System.out.println("   ➤ Trạng thái: " + trangThai);
                    System.out.println("   ➤ Ghi chú: " + (ghiChu == null ? "(Không có)" : ghiChu));
                    thongBaoThanhCong.add("✅ Đơn hàng " + maDH + " được cập nhật thành công!");
                }
            }
        }

        // Thêm thông báo để hiển thị lại ở trang
        model.addAttribute("successMessages", thongBaoThanhCong);
        model.addAttribute("errorMessages", thongBaoLoi);

        // Load lại danh sách để hiển thị tiếp
        hienThiDanhSachDonHangDangXuLy(model);

        return "shipper/process";
    }
}
