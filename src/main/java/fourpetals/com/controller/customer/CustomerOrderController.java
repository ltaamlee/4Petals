package fourpetals.com.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.response.customers.CustomerOrderResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.service.OrderService;

@Controller
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepo;

    private Customer getCustomerByPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Người dùng chưa đăng nhập");
        }

        String username = principal.getName();

        return customerRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng cho tài khoản: " + username));
    }

    @GetMapping
    public String viewOrders(@RequestParam(value = "status", required = false) String status,
                             Principal principal,
                             Model model) {
        Customer customer = getCustomerByPrincipal(principal);

        List<CustomerOrderResponse> orders;
        if (status == null || status.equalsIgnoreCase("tatca")) {
            orders = orderService.getOrdersByCustomer(customer);
        } else {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderService.getOrdersByCustomerAndStatus(customer, orderStatus)
                                 .stream()
                                 .map(o -> new CustomerOrderResponse(
                                         o.getMaDH(),
                                         o.getNgayDat(),
                                         o.getTongTien(),
                                         o.getTrangThai(),
                                         null // hoặc map sang chi tiết nếu cần
                                 ))
                                 .toList();
        }


        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);
        return "customer/order-tracking";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "customer/order-detail";
    }
}