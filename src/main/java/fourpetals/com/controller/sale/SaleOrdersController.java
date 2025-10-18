package fourpetals.com.controller.sale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.dto.response.orders.OrderResponse;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sale/orders")
@PreAuthorize("hasRole('SALES_EMPLOYEE')")
public class SaleOrdersController {

	@Autowired
	private OrderService orderService;

	// Thống kê đơn hàng
	@GetMapping("/stats")
	public Map<String, Long> getOrderStats() {
		LocalDateTime todayStart = LocalDate.now().atStartOfDay();
		LocalDateTime todayEnd = todayStart.plusDays(1).minusNanos(1);

		// Tổng số đơn hàng hôm nay
		long totalOrders = orderService.countByNgayDatBetween(todayStart, todayEnd);

		// Số đơn hàng cần xử lý (CHO_XU_LY)
		long pendingOrders = orderService.countByTrangThai(OrderStatus.CHO_XU_LY);

		// Số đơn hàng đã duyệt (DA_XAC_NHAN)
		long completedOrders = orderService.countByTrangThai(OrderStatus.DA_XAC_NHAN);

		// Số đơn hàng đã hủy (HUY)
		long cancelledOrders = orderService.countByTrangThai(OrderStatus.HUY);

		return Map.of("totalOrders", totalOrders, "pendingOrders", pendingOrders, "completedOrders", completedOrders,
				"cancelledOrders", cancelledOrders);
	}

	// Load danh sách đơn hàng
	@GetMapping
	public List<OrderResponse> getAllOrders() {
		return orderService.findAll().stream().map(OrderResponse::fromEntity).collect(Collectors.toList());
	}

	// Xem chi tiết đơn hàng
	@GetMapping("/view/{id}")
	public ResponseEntity<?> viewOrder(@PathVariable Integer id) {
		OrderDetailResponse orderDetail = orderService.getOrderDetail(id);
		if (orderDetail == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderDetail);
	}

	// Chỉnh sửa đơn hàng
	@PutMapping("/edit/{id}")
	public ResponseEntity<?> editOrder(@PathVariable("id") Integer id, @Valid @RequestBody OrderUpdateRequest request) {
		// Kiểm tra đơn hàng tồn tại
		Order existingOrder = orderService.findById(id);
		if (existingOrder == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng với ID " + id);
		}

		// Cập nhật đơn hàng
		request.setOrderId(id);
		Order updated = orderService.updateOrder(request);
		return ResponseEntity.ok(updated);
	}

}
