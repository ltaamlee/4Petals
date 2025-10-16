
package fourpetals.com.entity;

import java.math.BigDecimal;

import fourpetals.com.enums.ProductStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "SanPham")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", nullable = false, columnDefinition = "nvarchar(100)")
    private String tenSP;

    @Column(name = "DonViTinh", columnDefinition = "nvarchar(50)")
    private String donViTinh;

    @Column(name = "Gia", precision = 18, scale = 2, nullable = false)
    private BigDecimal gia;

    @Column(name = "SoLuongTon")
    private Integer soLuongTon;

    @Column(name = "MoTa", columnDefinition = "nvarchar(max)")
    private String moTa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "TrangThai", columnDefinition = "nvarchar(50)")
    private Integer trangThai = ProductStatus.DANG_BAN.getValue(); // Lưu số: 1, 0, -1

    @Column(name = "LuotXem")
    private Integer luotXem = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MaDM")
    private Category danhMuc;



    // ========== METHODS ĐỂ LÀM VIỆC VỚI ENUM ==========
    
    // Lấy ProductStatus enum từ giá trị số
    @Transient
    public ProductStatus getProductStatus() {
        return ProductStatus.fromValue(this.trangThai);
    }

    // Set ProductStatus enum (tự động convert sang số)
    public void setProductStatus(ProductStatus productStatus) {
        this.trangThai = productStatus.getValue();
    }

    // Tự động cập nhật trạng thái dựa vào số lượng tồn
    public void updateStatusBasedOnStock() {
        if (this.soLuongTon != null && this.soLuongTon <= 0) {
            this.setProductStatus(ProductStatus.HET_HANG);
        } else if (this.getProductStatus() == ProductStatus.HET_HANG && this.soLuongTon > 0) {
            this.setProductStatus(ProductStatus.DANG_BAN);
        }
    }

	public Integer getMaSP() {
		return maSP;
	}

	public void setMaSP(Integer maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public BigDecimal getGia() {
		return gia;
	}

	public void setGia(BigDecimal gia) {
		this.gia = gia;
	}

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Integer getLuotXem() {
		return luotXem;
	}

	public void setLuotXem(Integer luotXem) {
		this.luotXem = luotXem;
	}

	public Category getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(Category danhMuc) {
		this.danhMuc = danhMuc;
	}        
}
    