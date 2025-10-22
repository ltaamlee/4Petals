package fourpetals.com.dto.response.dashboard;

import java.math.BigDecimal;

public class DashboardSummaryResponse {
	private BigDecimal totalRevenue;
	private long totalEmployees;
	private long totalCustomers;
	private long totalOrders;

	// getters/setters/ctor
	public DashboardSummaryResponse() {
	}

	public DashboardSummaryResponse(BigDecimal rev, long emp, long cus, long ord) {
		this.totalRevenue = rev;
		this.totalEmployees = emp;
		this.totalCustomers = cus;
		this.totalOrders = ord;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal v) {
		this.totalRevenue = v;
	}

	public long getTotalEmployees() {
		return totalEmployees;
	}

	public void setTotalEmployees(long v) {
		this.totalEmployees = v;
	}

	public long getTotalCustomers() {
		return totalCustomers;
	}

	public void setTotalCustomers(long v) {
		this.totalCustomers = v;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long v) {
		this.totalOrders = v;
	}
}
