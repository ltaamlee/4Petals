// RecentOrderResponse.java
package fourpetals.com.dto.response.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecentOrderResponse {
	private Integer orderId;
	private String customerName;
	private BigDecimal totalAmount;
	private String paymentMethod;
	private String status;
	private LocalDateTime createdAt;

	// getters/setters/ctor
	public RecentOrderResponse() {
	}

	public RecentOrderResponse(Integer id, String cus, BigDecimal total, String pay, String st, LocalDateTime c) {
		this.orderId = id;
		this.customerName = cus;
		this.totalAmount = total;
		this.paymentMethod = pay;
		this.status = st;
		this.createdAt = c;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer v) {
		this.orderId = v;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String v) {
		this.customerName = v;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal v) {
		this.totalAmount = v;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String v) {
		this.paymentMethod = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		this.status = v;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime v) {
		this.createdAt = v;
	}
}
