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
import fourpetals.com.service.ReviewService;

@Controller
@RequestMapping("/customer/orders")
public class CustomerOrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerRepository customerService;

	@Autowired
	private ReviewService reviewService;

	private Customer getCustomerByPrincipal(Principal principal) {
		if (principal == null) {
			throw new RuntimeException("Người dùng chưa đăng nhập");
		}

		String username = principal.getName();

		return customerService.findByUser_Username(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng cho tài khoản: " + username));
	}

	@GetMapping
	public String viewOrders(@RequestParam(value = "status", required = false) String status, Principal principal,
			Model model) {
		Customer customer = getCustomerByPrincipal(principal);

		List<CustomerOrderResponse> orders;
		if (status == null || status.equalsIgnoreCase("tatca")) {
			orders = orderService.getOrdersByCustomer(customer);
		} else {
			OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
		    orders = orderService.getOrdersByCustomerAndStatusWithDetails(customer, orderStatus);
		}
		
		model.addAttribute("user", customer.getUser());
		model.addAttribute("orders", orders);
		model.addAttribute("selectedStatus", (status == null) ? "tatca" : status.toLowerCase());
		return "customer/order-tracking";
	}

	@GetMapping("/{id}")
	public String viewOrderDetail(@PathVariable("id") Integer id, Principal principal, Model model) {
		Customer customer = getCustomerByPrincipal(principal);
		Order order = orderService.getOrderById(id);

		if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
			throw new RuntimeException("Bạn không có quyền xem đơn hàng này");
		}

		model.addAttribute("order", order);
		model.addAttribute("details", order.getChiTietDonHang());
		return "customer/order-detail";
	}

	@PostMapping("/{id}/cancel")
	public String cancelOrder(@PathVariable("id") Integer id, Principal principal) {
		Customer customer = getCustomerByPrincipal(principal);
		Order order = orderService.getOrderById(id);

		if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
			throw new RuntimeException("Bạn không có quyền hủy đơn hàng này");
		}

		// chỉ được hủy khi đang chờ xử lý
		if (order.getTrangThai() != OrderStatus.CHO_XU_LY) {
			throw new RuntimeException("Đơn hàng đã được xử lý, không thể hủy");
		}

		order.setTrangThai(OrderStatus.HUY);
		orderService.save(order);

		return "redirect:/customer/orders?status=cho_xu_ly";
	}

	@PostMapping("/{id}/return")
	public String requestReturn(@PathVariable("id") Integer id, Principal principal) {
		Customer customer = getCustomerByPrincipal(principal);
		Order order = orderService.getOrderById(id);

		if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
			throw new RuntimeException("Bạn không có quyền thao tác với đơn hàng này");
		}

		if (order.getTrangThai() != OrderStatus.HOAN_TAT) {
			throw new RuntimeException("Chỉ có thể yêu cầu trả hàng cho đơn đã hoàn tất");
		}

		order.setTrangThai(OrderStatus.TRA_HANG);
		orderService.save(order);

		return "redirect:/customer/orders?status=hoan_tat";
	}

	@PostMapping("/{id}/review")
	@ResponseBody
	public String submitReview(@PathVariable("id") Integer id, @RequestParam("rating") int rating,
			@RequestParam("comment") String comment, Principal principal) {

		Customer customer = getCustomerByPrincipal(principal);
		Order order = orderService.getOrderById(id);

		if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
			throw new RuntimeException("Không có quyền đánh giá đơn này");
		}

		// 🔹 Nếu đơn có 1 sản phẩm -> đánh giá 1 sản phẩm
		if (order.getChiTietDonHang().size() == 1) {
			var detail = order.getChiTietDonHang().get(0);
			reviewService.addReview(detail.getSanPham(), customer.getUser(), rating, comment);
		}
		// 🔹 Nếu đơn có nhiều sản phẩm -> lặp qua từng sản phẩm
		else {
			order.getChiTietDonHang().forEach(detail -> {
				reviewService.addReview(detail.getSanPham(), customer.getUser(), rating, comment);
			});
		}

		return "success";
	}

}