package fourpetals.com.dto.response.promotions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.PromotionStatus;
import fourpetals.com.enums.PromotionType;

public class PromotionResponse {
    private Integer makm;
    private String tenkm;
    private PromotionType loaiKm;
    private PromotionStatus trangThai;
    private BigDecimal giaTri;
    private LocalDateTime thoiGianBd;
    private LocalDateTime thoiGianKt;
    private String moTa;

    //Loại khách hàng áp dụng
    private CustomerRank loaiKhachHang;
    
    // Danh sách sản phẩm áp dụng khuyến mãi
    private List<Integer> sanPhamIds;
    private List<String> sanPhamNames;
        
    
	public PromotionResponse() {
		super();
	}
	public PromotionResponse(Integer makm, String tenkm, PromotionType loaiKm, PromotionStatus trangThai,
			BigDecimal giaTri, LocalDateTime thoiGianBd, LocalDateTime thoiGianKt, String moTa,
			CustomerRank loaiKhachHang, List<Integer> sanPhamIds, List<String> sanPhamNames) {
		super();
		this.makm = makm;
		this.tenkm = tenkm;
		this.loaiKm = loaiKm;
		this.trangThai = trangThai;
		this.giaTri = giaTri;
		this.thoiGianBd = thoiGianBd;
		this.thoiGianKt = thoiGianKt;
		this.moTa = moTa;
		this.loaiKhachHang = loaiKhachHang;
		this.sanPhamIds = sanPhamIds;
		this.sanPhamNames = sanPhamNames;
	}
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
	public List<String> getSanPhamNames() {
		return sanPhamNames;
	}
	public void setSanPhamNames(List<String> sanPhamNames) {
		this.sanPhamNames = sanPhamNames;
	}
    
      
	
}
