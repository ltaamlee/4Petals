package fourpetals.com.entity;

import java.math.BigDecimal;

import fourpetals.com.enums.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", length = 100, nullable = false)
    private String tenSP;

    @Column(name = "DonViTinh", length = 50)
    private String donViTinh;

    @Column(name = "Gia", precision = 18, scale = 2, nullable = false)
    private BigDecimal gia;

    @Column(name = "SoLuongTon")
    private Integer soLuongTon;

    @Column(name = "MoTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "TrangThai")
    private Integer trangThai = ProductStatus.DANG_BAN.getValue(); // Lưu số: 1, 0, -1

    @Column(name = "LuotXem")
    private Integer luotXem = 0;

    @ManyToOne
    @JoinColumn(name = "MaDM")
    private Category danhMuc;

    @ManyToOne
    @JoinColumn(name = "MaNCC")
    private Supplier nhaCungCap;

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
}