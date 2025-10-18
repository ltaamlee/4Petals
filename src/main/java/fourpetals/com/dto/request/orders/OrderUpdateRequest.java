package fourpetals.com.dto.request.orders;

import java.math.BigDecimal;
import java.time.LocalDate;

import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OrderUpdateRequest {

	private Integer orderId;
	
	private OrderStatus trangThai;
	
	@FutureOrPresent(message = "Ngày giao phải là hôm nay hoặc trong tương lai")
	private LocalDate ngayGiao;
	
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
	private String diaChiGiao;
	
	@NotBlank(message = "Số điện thoại người nhận không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
	private String sdtNguoiNhan;
	
	private PaymentMethod phuongThucThanhToan;
	
    @DecimalMin(value = "0.0", inclusive = true, message = "Phí vận chuyển không được âm")
	private BigDecimal phiVanChuyen;
    
    @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
	private String ghiChu;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public OrderStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(OrderStatus trangThai) {
		this.trangThai = trangThai;
	}

	public LocalDate getNgayGiao() {
		return ngayGiao;
	}

	public void setNgayGiao(LocalDate ngayGiao) {
		this.ngayGiao = ngayGiao;
	}

	public String getDiaChiGiao() {
		return diaChiGiao;
	}

	public void setDiaChiGiao(String diaChiGiao) {
		this.diaChiGiao = diaChiGiao;
	}

	public String getSdtNguoiNhan() {
		return sdtNguoiNhan;
	}

	public void setSdtNguoiNhan(String sdtNguoiNhan) {
		this.sdtNguoiNhan = sdtNguoiNhan;
	}

	public PaymentMethod getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}

	public void setPhuongThucThanhToan(PaymentMethod phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}

	public BigDecimal getPhiVanChuyen() {
		return phiVanChuyen;
	}

	public void setPhiVanChuyen(BigDecimal phiVanChuyen) {
		this.phiVanChuyen = phiVanChuyen;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

}
