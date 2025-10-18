package fourpetals.com.controller.inventory;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.controller.OrderDetailDTO;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.repository.OrderRepository;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> listOrders = orderRepository.findAll();

        model.addAttribute("listOrders", listOrders);
        return "inventory/orders"; // orders.html
    }

	// -------------------- Xem chi tiết đơn hàng--------------------
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
