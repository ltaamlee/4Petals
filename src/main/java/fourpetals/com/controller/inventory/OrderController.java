package fourpetals.com.controller.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.controller.OrderDetailDTO;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.OrderRepository;

@Controller
@RequestMapping("inventory/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> listOrders = orderRepository.findAllConfirmedOrders();
        model.addAttribute("listOrders", listOrders);

        return "inventory/orders"; 
    }


    // ✅ 2. Lấy chi tiết đơn hàng thực trong DB 
    @Transactional
    @GetMapping("/{maDH}/details")
    @ResponseBody
    public List<OrderDetailDTO> getOrderDetails(@PathVariable Integer maDH) {
        return orderRepository.findById(maDH)
                .map(order -> order.getChiTietDonHang().stream()
                        .map(ct -> new OrderDetailDTO(
                                ct.getSanPham().getTenSP(),
                                ct.getSoLuong(),
                                ct.getGiaBan()
                        ))
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    // ✅ 3. Cập nhật trạng thái đơn hàng sang "ĐÃ ĐÓNG ĐƠN" và lưu vào DB
    @PostMapping("/{maDH}/confirm")
    @ResponseBody
    @Transactional
    public String confirmOrder(@PathVariable Integer maDH) {
        return orderRepository.findById(maDH)
                .map(order -> {
                    order.setTrangThai(OrderStatus.DA_DONG_DON);
                    orderRepository.save(order);
                    return "Đơn hàng " + maDH + " đã được chuyển sang trạng thái Đã đóng đơn.";
                })
                .orElse("Không tìm thấy đơn hàng có mã " + maDH);
    }
}
