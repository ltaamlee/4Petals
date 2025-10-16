package fourpetals.com.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.entity.*;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import fourpetals.com.repository.*;
import fourpetals.com.service.CartService;
import fourpetals.com.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private CartService cartService;
    

    @Transactional
    @Override
    public Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu) {
        
    	User user = customer.getUser();
    	List<Cart> cartItems = cartService.getCartByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng.");
        }

        // Tổng tiền hàng
        BigDecimal tongTien = cartItems.stream()
                .map(Cart::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tạo đơn hàng
        Order order = new Order();
        order.setKhachHang(customer);
        order.setDiaChiGiao(diaChi);
        order.setSdtNguoiNhan(sdt);
        order.setTongTien(tongTien);
        order.setPhiVanChuyen(BigDecimal.ZERO);
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
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findByKhachHang(customer);
    }
}
