package fourpetals.com.dto.request.promotions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PromotionUpdateRequest {
	private Integer makm; // bắt buộc để biết update khuyến mãi nào
	
	@NotBlank(message = "Tên khuyến mãi không được để trống")
	@Size(min = 3, max = 100, message = "Tên khuyến mãi phải từ 3-100 ký tự")
	private String tenkm;
	
	@NotNull(message = "Vui lòng chọn loại khuyến mãi")
	private PromotionType loaiKm;
	
	private PromotionStatus trangThai = PromotionStatus.INACTIVE;

	@DecimalMin(value = "0.0", inclusive = false, message = "Giá trị phải lớn hơn 0")
	private BigDecimal giaTri;
	
	@NotNull(message = "Thời gian bắt đầu không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime thoiGianBd;
	
	@NotNull(message = "Thời gian kết thúc không được để trống")
	@Future(message = "Thời gian kết thúc phải là thời điểm tương lai")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime thoiGianKt;
	
	@Size(max = 500, message = "Mô tả tối đa 500 ký tự")
	private String moTa;
	private CustomerRank loaiKhachHang;
	private List<Integer> sanPhamIds;

	public Integer getMakm() {
		return makm;
	}

	public void setMakm(Integer makm) {
		this.makm = makm;
	}

	public String getTenkm() {
		return tenkm;
	}

	public void setTenkm(String tenkm) {
		this.tenkm = tenkm;
	}

	public PromotionType getLoaiKm() {
		return loaiKm;
	}

	public void setLoaiKm(PromotionType loaiKm) {
		this.loaiKm = loaiKm;
	}

	public PromotionStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(PromotionStatus trangThai) {
		this.trangThai = trangThai;
	}

	public BigDecimal getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(BigDecimal giaTri) {
		this.giaTri = giaTri;
	}

	public LocalDateTime getThoiGianBd() {
		return thoiGianBd;
	}

	public void setThoiGianBd(LocalDateTime thoiGianBd) {
		this.thoiGianBd = thoiGianBd;
	}

	public LocalDateTime getThoiGianKt() {
		return thoiGianKt;
	}

	public void setThoiGianKt(LocalDateTime thoiGianKt) {
		this.thoiGianKt = thoiGianKt;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	

	public CustomerRank getLoaiKhachHang() {
		return loaiKhachHang;
	}

	public void setLoaiKhachHang(CustomerRank loaiKhachHang) {
		this.loaiKhachHang = loaiKhachHang;
	}

	public List<Integer> getSanPhamIds() {
		return sanPhamIds;
	}

	public void setSanPhamIds(List<Integer> sanPhamIds) {
		this.sanPhamIds = sanPhamIds;
	}

}
