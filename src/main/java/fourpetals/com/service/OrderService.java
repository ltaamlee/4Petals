package fourpetals.com.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;

public interface OrderService {
	// Thống kê
	long countByTrangThai(OrderStatus trangThai);
	long countByNgayDatBetween(LocalDateTime from, LocalDateTime to);
	Map<LocalDate, Long> countByDate(int recentDays);
	
	// CRUD
	Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu);
	Order save(Order o);
	void delete(Integer id);
    Order updateOrder(OrderUpdateRequest request);

	
	// Tìm kiếm
	Order findById(Integer id);
	Order changeStatus(Integer id, OrderStatus next);
	List<Order> findAll();
	List<Order> getOrdersByKhachHang(Customer customer);

	OrderDetailResponse getOrderDetail(Integer maDH);

	
	
	//DÀNH CHO NHẬP KHO
	List<Order> findAllConfirmedOrders();

	
	
}
