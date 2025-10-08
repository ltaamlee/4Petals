package fourpetals.com.controller.inventory;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.controller.OrderDetailDTO;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public String listOrders(Model model) {
        // 2 đơn hàng mẫu
        Customer kh1 = new Customer();
        kh1.setMaKH(1);
        kh1.setHoTen("Nguyen Van A");

        Customer kh2 = new Customer();
        kh2.setMaKH(2);
        kh2.setHoTen("Tran Thi B");

        Order o1 = new Order();
        o1.setMaDH(101);
        o1.setKhachHang(kh1);

        Order o2 = new Order();
        o2.setMaDH(102);
        o2.setKhachHang(kh2);

        List<Order> listOrders = List.of(o1, o2);
        model.addAttribute("listOrders", listOrders);

        return "inventory/orders"; // trỏ tới orders.html
    }

    // Lấy chi tiết đơn hàng (JSON)
    @GetMapping("/{maDH}/details")
    @ResponseBody
    public List<OrderDetailDTO> getOrderDetails(@PathVariable Integer maDH) {
        if (maDH == 101) {
            return List.of(
                new OrderDetailDTO("Hoa hồng đỏ", 2, new BigDecimal("100000")),
                new OrderDetailDTO("Hoa lan vàng", 1, new BigDecimal("150000"))
            );
        } else {
            return List.of(
                new OrderDetailDTO("Hoa cẩm chướng", 3, new BigDecimal("50000")),
                new OrderDetailDTO("Hoa hướng dương", 4, new BigDecimal("75000"))
            );
        }
    }

    // Xác nhận đơn hàng (dummy, không cập nhật database)
    @PostMapping("/{maDH}/confirm")
    @ResponseBody
    public void confirmOrder(@PathVariable Integer maDH) {
        System.out.println("Đơn hàng " + maDH + " đã xác nhận (test).");
    }
}
