package fourpetals.com.service;

import java.math.BigDecimal;

import fourpetals.com.dto.response.customers.MomoCreateResponseDto;
import fourpetals.com.dto.response.customers.MomoPaymentResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;

public interface MomoService {

	MomoCreateResponseDto createPayment(Order order);

	void handleCallback(String orderId, int resultCode);

	MomoPaymentResponse createQuickPayment(BigDecimal amount, String orderInfo);

	boolean handleMomoReturn(String orderId, int resultCode);

	Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu);
}
