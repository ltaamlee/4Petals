package fourpetals.com.dto.request.orders;


import jakarta.validation.constraints.Size;

public class OrderUpdateRequest {

	private Integer orderId;
	
    @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
	private String ghiChu;
    

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	
}
