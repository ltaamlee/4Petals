package fourpetals.com.controller.manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/manager/orders")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerOrdersController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;


	// ─────────────────────────────────────────────────────────────────────────
    // 1) Stats (giữ lại nhưng tối giản, không liên quan duyệt/hủy)
    // ─────────────────────────────────────────────────────────────────────────
	@GetMapping("/stats")
	public Map<String, Long> getOrderStats() {
	    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
	    LocalDateTime todayEnd   = todayStart.plusDays(1).minusNanos(1);

	    long totalOrdersToday = orderService.countByNgayDatBetween(todayStart, todayEnd);
	    long closedOrders     = orderService.countByTrangThai(OrderStatus.DA_DONG_DON);
	    long deliveringOrders = orderService.countByTrangThai(OrderStatus.DANG_GIAO);
	    long cancelledOrders  = orderService.countByTrangThai(OrderStatus.HUY);

	    return Map.of(
	        "totalOrders",      totalOrdersToday,
	        "closedOrders",     closedOrders,
	        "deliveringOrders", deliveringOrders,
	        "cancelledOrders",  cancelledOrders
	    );
	}

    // ─────────────────────────────────────────────────────────────────────────
    // 2) Danh sách chỉ ĐÃ_ĐÓNG_ĐƠN (bỏ tham số trangThai để tránh nhầm)
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    public Page<OrderResponse> getOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        // Ép trạng thái cố định = DA_DONG_DON
        return orderService.filterOrders(OrderStatus.DA_DONG_DON.name(), keyword, pageable);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 3) Xem chi tiết đơn
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewOrder(@PathVariable Integer id) {
        OrderDetailResponse orderDetail = orderService.getOrderDetail(id);
        if (orderDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetail);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 4) Danh sách shipper đơn giản (id + name) để hiện lên dropdown
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/shippers")
    public ResponseEntity<List<ShipperOption>> listAvailableShippers() {
        List<Employee> shippers = userService.findShippers();
        List<ShipperOption> result = shippers.stream()
                .map(e -> new ShipperOption(e.getMaNV(), e.getHoTen()))
                .toList();
        return ResponseEntity.ok(result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 5) Phân công shipper cho đơn "ĐÃ_ĐÓNG_ĐƠN" -> chuyển "ĐANG_GIAO"
    // ─────────────────────────────────────────────────────────────────────────
    @PutMapping("/{orderId}/assign-shipper")
    public ResponseEntity<?> assignShipper(
            @PathVariable Integer orderId,
            @RequestParam("employeeId") @NotNull Integer employeeId) {

        // Lấy đơn
        Order order = orderService.findById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Không tìm thấy đơn hàng với ID " + orderId));
        }

        // Chỉ cho phép phân công khi đang ở trạng thái ĐÃ_ĐÓNG_ĐƠN
        if (order.getTrangThai() != OrderStatus.DA_DONG_DON) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Chỉ phân công shipper cho đơn ở trạng thái ĐÃ ĐÓNG ĐƠN."));
        }

        // Kiểm tra shipper tồn tại
        Employee shipper = userService.findEmployeeById(employeeId);
        if (shipper == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Không tìm thấy nhân viên giao hàng (ID: " + employeeId + ")."));
        }

        // Gán shipper + chuyển trạng thái
        order.setNhanVienGiaoHang(shipper);
        order.setTrangThai(OrderStatus.DANG_GIAO);
        order.setNgayCapNhat(LocalDateTime.now());

        // Lưu bằng service (đặt tên method mới để tránh đụng với updateOrder cũ)
        Order saved = orderService.saveAssignedShipper(order);

        // Trả về chi tiết sau khi phân công để FE refresh
        OrderDetailResponse response = orderService.getOrderDetail(saved.getMaDH());
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DTO nội bộ cho dropdown shipper (tránh kéo cả entity ra ngoài)
    // ─────────────────────────────────────────────────────────────────────────
    public static record ShipperOption(Integer id, String name) { }
	
}
