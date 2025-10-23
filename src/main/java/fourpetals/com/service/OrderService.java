package fourpetals.com.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fourpetals.com.dto.request.orders.OrderUpdateRequest;
import fourpetals.com.dto.response.customers.CustomerOrderResponse;
import fourpetals.com.dto.response.orders.OrderDetailResponse;
import fourpetals.com.dto.response.orders.OrderResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.entity.Product;
import fourpetals.com.enums.OrderStatus;

public interface OrderService {
	

    Order saveAssignedShipper(Order order);     
	// Thống kê
	long countByTrangThai(OrderStatus trangThai);

	long countByNgayDatBetween(LocalDateTime from, LocalDateTime to);

	Map<LocalDate, Long> countByDate(int recentDays);

	// CRUD
	Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu);

	Order createOrder(Customer customer, Product product, int quantity, String tenNguoiNhan, String sdt, String diaChi,
			String ghiChu);
	
	Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu, List<Integer> cartIds);


	Order save(Order o);

	void delete(Integer id);

	Order updateOrder(OrderUpdateRequest request);

	// Tìm kiếm
	Order findById(Integer id);

	Order changeStatus(Integer id, OrderStatus next);

	List<Order> findAll();

	List<Order> getOrdersByKhachHang(Customer customer);

	OrderDetailResponse getOrderDetail(Integer maDH);

	// Tìm kiếm + Lọc phân trang
	Page<OrderResponse> filterOrders(String trangThai, String keyword, Pageable pageable);

	// DÀNH CHO NHẬP KHO
	List<Order> findAllConfirmedOrders();

	// DUYỆT ĐƠN HÀNG / HỦY ĐƠN HÀNG

	// ĐÓNG GÓI ĐƠN HÀNG
	List<CustomerOrderResponse> getOrdersByCustomer(Customer customer);

	List<Order> getOrdersByCustomerAndStatus(Customer customer, OrderStatus status);

	List<CustomerOrderResponse> getOrdersByCustomerAndStatusWithDetails(Customer customer, OrderStatus status);

	Order getOrderById(Integer id);

	Page<OrderResponse> findClosedOrdersEnum(String keyword, Pageable pageable);
	boolean cancelOrder(Integer orderId, Integer userId);
	
}
