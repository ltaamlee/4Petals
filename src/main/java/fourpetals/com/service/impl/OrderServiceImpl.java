package fourpetals.com.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.dto.response.orders.OrderResponse;
import fourpetals.com.dto.response.customers.CustomerOrderResponse;
import fourpetals.com.dto.response.customers.OrderItemDTO;
import fourpetals.com.entity.*;
import fourpetals.com.enums.CancelRequestStatus;
import fourpetals.com.enums.NotificationType;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import fourpetals.com.enums.RoleName;
import fourpetals.com.enums.ShippingFee;
import fourpetals.com.repository.*;
import fourpetals.com.service.CartService;
import fourpetals.com.service.NotificationService;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.ShippingService;
import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private ShippingService shippingService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	// ✅ 1. Tạo đơn hàng từ giỏ hàng (COD hoặc MoMo đều dùng được)
    @Override
    @Transactional
    public Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu) {
        User user = customer.getUser();
        List<Cart> cartItems = cartService.getCartByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng.");
        }

        // Tổng tiền hàng
        BigDecimal tongTienHang = cartItems.stream()
                .map(Cart::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Phí ship cố định (hoặc có thể gọi ShippingService)
        BigDecimal phiVanChuyen = BigDecimal.valueOf(30000);

        Order order = new Order();
        order.setKhachHang(customer);
        order.setDiaChiGiao(diaChi);
        order.setSdtNguoiNhan(sdt);
        order.setPhiVanChuyen(phiVanChuyen);
        order.setTongTien(tongTienHang.add(phiVanChuyen));
        order.setPhuongThucThanhToan(PaymentMethod.COD);
        order.setTrangThai(OrderStatus.CHO_XU_LY);
        order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
        order.setGhiChu(ghiChu);
        order.setNgayDat(LocalDateTime.now());

        // Thêm chi tiết đơn hàng
        List<OrderDetail> details = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setDonHang(order);
            Product managedProduct = entityManager.getReference(Product.class, item.getSanPham().getMaSP());
            detail.setSanPham(managedProduct);

            detail.setSoLuong(item.getSoLuong());
            detail.setGiaBan(item.getSanPham().getGia());
            return detail;
        }).toList();

        order.setChiTietDonHang(details);
        orderRepository.save(order);
        return order;
    }

    // ✅ 2. Tạo đơn hàng “mua nhanh” 1 sản phẩm (MoMo)
    @Override
    @Transactional
    public Order createOrder(Customer customer, Product product, int quantity,
                             String tenNguoiNhan, String sdt, String diaChi, String ghiChu) {

        Order order = new Order();
        order.setKhachHang(customer);
        order.setDiaChiGiao(diaChi);
        order.setSdtNguoiNhan(sdt);
        order.setGhiChu(ghiChu);
        order.setNgayDat(LocalDateTime.now());
        order.setTrangThai(OrderStatus.CHO_XU_LY);
        order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);

        BigDecimal tongTien = product.getGia().multiply(BigDecimal.valueOf(quantity));
        BigDecimal phiVanChuyen = BigDecimal.valueOf(30000);
        order.setPhiVanChuyen(phiVanChuyen);
        order.setTongTien(tongTien.add(phiVanChuyen));

        OrderDetail detail = new OrderDetail();
        detail.setDonHang(order);
        Product managedProduct = entityManager.getReference(Product.class, product.getMaSP());
        detail.setSanPham(managedProduct);
        detail.setSoLuong(quantity);
        detail.setGiaBan(product.getGia());

        order.setChiTietDonHang(List.of(detail));
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional
    public Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu, List<Integer> cartIds) {
        User user = customer.getUser();
        if (cartIds == null || cartIds.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm nào được chọn để đặt hàng.");
        }

        // 🔹 Chỉ lấy các sản phẩm được chọn
        List<Cart> cartItems = cartService.getCartByIds(cartIds);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng.");
        }

        BigDecimal tongTienHang = cartItems.stream()
                .map(Cart::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal phiVanChuyen = BigDecimal.valueOf(30000);

        Order order = new Order();
        order.setKhachHang(customer);
        order.setDiaChiGiao(diaChi);
        order.setSdtNguoiNhan(sdt);
        order.setPhiVanChuyen(phiVanChuyen);
        order.setTongTien(tongTienHang.add(phiVanChuyen));
        order.setPhuongThucThanhToan(PaymentMethod.COD);
        order.setTrangThai(OrderStatus.CHO_XU_LY);
        order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
        order.setGhiChu(ghiChu);
        order.setNgayDat(LocalDateTime.now());

        // 🔹 Chỉ thêm chi tiết của các sản phẩm được chọn
        List<OrderDetail> details = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setDonHang(order);
            Product managedProduct = entityManager.getReference(Product.class, item.getSanPham().getMaSP());
            detail.setSanPham(managedProduct);
            detail.setSoLuong(item.getSoLuong());
            detail.setGiaBan(item.getSanPham().getGia());
            return detail;
        }).toList();

        order.setChiTietDonHang(details);
        orderRepository.save(order);

        // ❌ Không xóa toàn bộ giỏ
        // ✅ Chỉ xóa các sản phẩm đã chọn (đã xử lý trong controller sau khi thanh toán thành công)
        return order;
    }



	@Override
	public List<Order> getOrdersByKhachHang(Customer customer) {
		return orderRepository.findByKhachHang(customer);
	}

	@Override
	public Order findById(Integer id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public Order changeStatus(Integer id, OrderStatus next) {
		Order order = findById(id);
		if (order != null && next != null) {
			OrderStatus current = order.getTrangThai();

			// Kiểm tra xem trạng thái tiếp theo hợp lệ theo flow
			if (next == current.getNextStatus() || (next == OrderStatus.HUY && current.canCancel())) {

				order.setTrangThai(next);
				return orderRepository.save(order);
			}
		}
		return null; // Nếu không hợp lệ hoặc không tìm thấy đơn
	}

	@Override
	public void delete(Integer id) {
		orderRepository.deleteById(id);
	}

	@Override
	public long countByTrangThai(OrderStatus trangThai) {
		return orderRepository.countByTrangThai(trangThai);
	}

	@Override
	public long countByNgayDatBetween(LocalDateTime from, LocalDateTime to) {
		return orderRepository.countByNgayDatBetween(from, to);
	}

	@Override
	public Map<LocalDate, Long> countByDate(int recentDays) {
		Map<LocalDate, Long> result = new LinkedHashMap<>();
		LocalDate today = LocalDate.now();
		for (int i = 0; i < recentDays; i++) {
			LocalDate date = today.minusDays(i);
			long count = orderRepository.countByNgayDatBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
			result.put(date, count);
		}
		return result;
	}

	@Override
	public Order save(Order o) {
		return orderRepository.save(o);
	}

	@Transactional(readOnly = true)
	public OrderDetailResponse getOrderDetail(Integer orderId) {
		Optional<Order> optOrder = orderRepository.findByIdWithDetails(orderId);
		if (optOrder.isEmpty()) {
			return null;
		}
		return OrderDetailResponse.fromEntity(optOrder.get());
	}

	@Override
	public Order updateOrder(OrderUpdateRequest request) {
		Order order = findById(request.getOrderId());
		if (order == null)
			return null;

		order.setGhiChu(request.getGhiChu());
		return orderRepository.save(order);
	}

	@Override
	public List<Order> findAllConfirmedOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<OrderResponse> filterOrders(String trangThai, String keyword, Pageable pageable) {
		OrderStatus statusEnum = null;

		// Convert filter trạng thái từ String sang Enum
		if (trangThai != null && !trangThai.isEmpty()) {
			try {
				statusEnum = OrderStatus.valueOf(trangThai);
			} catch (IllegalArgumentException e) {
				statusEnum = null; // nếu value không hợp lệ, bỏ filter
			}
		}

		if (keyword != null && !keyword.isEmpty()) {
			for (OrderStatus status : OrderStatus.values()) {
				if (status.getDisplayName().toLowerCase().contains(keyword.toLowerCase())) {
					statusEnum = status;
					keyword = null;
					break;
				}
			}
		}

		return orderRepository.filterOrders(statusEnum, keyword, pageable).map(OrderResponse::fromEntity);
	}

	@Override
	public boolean createCancelRequest(Integer orderId, Integer senderId, String reason) {
		// Lấy đơn hàng
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

		// Kiểm tra trạng thái
		if (!order.getTrangThai().canCancel()) {
			throw new RuntimeException("Đơn hàng đang ở trạng thái không thể hủy");
		}
		if (order.getCancelRequestStatus() != CancelRequestStatus.NONE) {
			throw new RuntimeException("Đơn hàng đã có yêu cầu hủy trước đó");
		}

		// Cập nhật trạng thái yêu cầu hủy
		order.setCancelRequestStatus(CancelRequestStatus.PENDING);
		String oldNote = order.getGhiChu() != null ? order.getGhiChu() + "\n" : "";
		order.setGhiChu(oldNote + "Lý do hủy: " + reason);
		orderRepository.save(order);

		// Gửi event WebSocket realtime
		Map<String, Object> payload = new HashMap<>();
		payload.put("orderId", orderId);
		payload.put("cancelRequestStatus", order.getCancelRequestStatus().name());
		messagingTemplate.convertAndSend("/topic/order-cancel-status", payload);

		return true;
	}

	private CustomerOrderResponse mapToCustomerOrderResponse(Order order) {
		List<OrderItemDTO> items = order
				.getChiTietDonHang().stream().map(detail -> new OrderItemDTO(detail.getSanPham().getMaSP(),detail.getSanPham().getTenSP(),
						detail.getSoLuong(), detail.getGiaBan(), detail.getSanPham().getHinhAnh()))
				.collect(Collectors.toList());

		return new CustomerOrderResponse(order.getMaDH(), order.getNgayDat(), order.getTongTien(), order.getTrangThai(),
				items);
	}

	// ===== Lấy tất cả đơn hàng của khách (trả về DTO) =====
	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderResponse> getOrdersByCustomer(Customer customer) {
	    if (customer == null) return Collections.emptyList();

	    // 🔹 Sử dụng hàm có sắp xếp giảm dần
	    List<Order> orders = orderRepository.findByKhachHangOrderByNgayDatDesc(customer);

	    return orders.stream()
	                 .map(this::mapToCustomerOrderResponse)
	                 .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> getOrdersByCustomerAndStatus(Customer customer, OrderStatus status) {
	    // 🔹 Cũng sắp xếp theo ngày giảm dần
	    return orderRepository.findByKhachHangAndTrangThaiOrderByNgayDatDesc(customer, status);
	}


	@Override
    @Transactional(readOnly = true)
    public Order getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Ép Hibernate load danh sách chi tiết đơn hàng trước khi session đóng
        order.getChiTietDonHang().size();

        return order;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderResponse> getOrdersByCustomerAndStatusWithDetails(Customer customer, OrderStatus status) {
	    List<Order> orders = orderRepository.findByKhachHangAndTrangThaiOrderByNgayDatDesc(customer, status);
	    return orders.stream()
	            .map(this::mapToCustomerOrderResponse)
	            .collect(Collectors.toList());
	}


	// ===== Map entity → DTO =====
	private OrderResponse mapToOrderResponse(Order order) {
		List<OrderItemDTO> items = order.getChiTietDonHang().stream().map(detail -> mapToOrderItemDTO(detail))
				.collect(Collectors.toList());

		return new OrderResponse(order.getMaDH(), order.getNgayDat(), order.getTongTien(), order.getTrangThai(), items);
	}

	private OrderItemDTO mapToOrderItemDTO(OrderDetail detail) {
		return new OrderItemDTO(detail.getSanPham().getMaSP(),detail.getSanPham().getTenSP(), detail.getSoLuong(), detail.getGiaBan(),
				detail.getSanPham().getHinhAnh());
	}

}
