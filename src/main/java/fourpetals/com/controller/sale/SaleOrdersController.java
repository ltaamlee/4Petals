package fourpetals.com.controller.sale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.dto.response.orders.OrderResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Order;
import fourpetals.com.entity.User;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sale/orders")
@PreAuthorize("hasRole('SALES_EMPLOYEE')")
public class SaleOrdersController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

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

	@GetMapping
	public Page<OrderResponse> getOrders(@RequestParam(required = false) String trangThai,
			@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return orderService.filterOrders(trangThai, keyword, pageable);
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

	// Duyệt đơn hàng
	@PutMapping("/approve/{id}")
	public ResponseEntity<?> approveOrder(@PathVariable Integer id,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User không tồn tại");
		}

		User user = userOpt.get();
		Employee nhanVien = user.getNhanVien();
		if (nhanVien == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User chưa gán nhân viên");
		}

		Order order = orderService.findById(id);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng với ID " + id);
		}

		if (!order.getTrangThai().canConfirm()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đơn hàng không thể duyệt ở trạng thái hiện tại");
		}

		// Gán nhân viên và chuyển trạng thái
		order.setNhanVien(nhanVien);
		order.setTrangThai(order.getTrangThai().getNextStatus());
		order.setNgayCapNhat(LocalDateTime.now());
		orderService.save(order);

		// Trả về DTO để client render chi tiết
		OrderDetailResponse response = orderService.getOrderDetail(id);
		return ResponseEntity.ok(response);
	}

	// Yêu cầu hủy đơn và gửi lên quản lý
	@PostMapping("/{orderId}/request-cancel")
	public ResponseEntity<?> requestCancelOrder(@PathVariable Integer orderId, @RequestBody Map<String, String> body,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		String reason = body.get("reason");
		if (reason == null || reason.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Lý do hủy không được để trống")); 
		}

		Integer senderId = userDetails.getUser().getUserId(); // lấy ID người đang đăng nhập

		try {
			boolean success = orderService.createCancelRequest(orderId, senderId, reason);
			if (success) {
				return ResponseEntity.ok(Map.of("message", "Yêu cầu hủy đã gửi đến quản lý"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("message", "Không thể gửi yêu cầu hủy"));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
		}
	}

}
