package fourpetals.com.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.entity.*;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import fourpetals.com.enums.ShippingFee;
import fourpetals.com.repository.*;
import fourpetals.com.service.CartService;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.ShippingService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private ShippingService shippingService;

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Transactional
	@Override
	public Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu) {

		User user = customer.getUser();
		List<Cart> cartItems = cartService.getCartByUser(user);
		if (cartItems.isEmpty()) {
			throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng.");
		}

		// Tổng tiền hàng trước phí vận chuyển
		BigDecimal tongTienHang = cartItems.stream().map(Cart::getTongTien).reduce(BigDecimal.ZERO, BigDecimal::add);

		// Xác định loại vận chuyển (ví dụ gán cứng theo địa chỉ)
		ShippingFee shippingType = ShippingFee.NOI_THANH; // có thể mở rộng logic để xác định tự động
		BigDecimal phiVanChuyen = shippingService.getFee(shippingType);
		System.out.println("Phí vận chuyển gán: " + phiVanChuyen);


		// Tạo đơn hàng
		Order order = new Order();
		order.setKhachHang(customer);
		order.setDiaChiGiao(diaChi);
		order.setSdtNguoiNhan(sdt);
		order.setPhiVanChuyen(phiVanChuyen);
		order.setTongTien(tongTienHang.add(phiVanChuyen)); // tổng = hàng + phí vận chuyển
		order.setPhuongThucThanhToan(PaymentMethod.COD);
		order.setTrangThai(OrderStatus.CHO_XU_LY);
		order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
		order.setGhiChu(ghiChu);

		order = orderRepository.save(order);

		// Thêm chi tiết đơn hàng
		for (Cart item : cartItems) {
			OrderDetail detail = new OrderDetail();
			detail.setId(new OrderDetailId(order.getMaDH(), item.getSanPham().getMaSP()));
			detail.setDonHang(order);
			detail.setSanPham(item.getSanPham());
			detail.setSoLuong(item.getSoLuong());
			detail.setGiaBan(item.getSanPham().getGia());
			orderDetailRepository.save(detail);
		}

		// Xóa giỏ hàng
		cartService.clearCart(user);

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
	@Transactional(readOnly = true)
    public Order updateOrder(OrderUpdateRequest request) {
        Order order = findById(request.getOrderId());
        if (order == null) return null;

        // Cập nhật thông tin đơn hàng
        order.setTrangThai(request.getTrangThai() != null ? request.getTrangThai() : order.getTrangThai());
        order.setNgayGiao(request.getNgayGiao() != null ? request.getNgayGiao() : order.getNgayGiao());
        order.setDiaChiGiao(request.getDiaChiGiao());
        order.setSdtNguoiNhan(request.getSdtNguoiNhan());
        order.setPhuongThucThanhToan(request.getPhuongThucThanhToan() != null ? request.getPhuongThucThanhToan() : order.getPhuongThucThanhToan());
        order.setPhiVanChuyen(request.getPhiVanChuyen() != null ? request.getPhiVanChuyen() : order.getPhiVanChuyen());
        order.setGhiChu(request.getGhiChu());

        // Nếu muốn cập nhật chi tiết, bạn có thể xóa các chi tiết cũ và thêm chi tiết mới
        // Ví dụ: orderDetailRepository.deleteAllByOrderId(order.getOrderId());

        return orderRepository.save(order);
    }

	@Override
	public List<Order> findAllConfirmedOrders() {
		// TODO Auto-generated method stub
		return null;
	}
}
