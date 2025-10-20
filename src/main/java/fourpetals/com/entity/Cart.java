package fourpetals.com.entity;

import java.math.BigDecimal;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GioHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGH")
    private Integer maGH;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User nguoiDung; // người sở hữu giỏ hàng

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    private Product sanPham; // sản phẩm trong giỏ

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    // ========== METHODS ==========
    // Tự động tính tổng tiền dựa vào giá và số lượng
    public void capNhatTongTien() {
        if (this.sanPham != null && this.sanPham.getGia() != null && this.soLuong != null) {
            this.tongTien = this.sanPham.getGia().multiply(new BigDecimal(this.soLuong));
        }
    }

    // ========== GETTER / SETTER ==========
    public Integer getMaGH() {
        return maGH;
    }

    public void setMaGH(Integer maGH) {
        this.maGH = maGH;
    }

    public User getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(User user) {
        this.nguoiDung = user;
    }

    public Product getSanPham() {
        return sanPham;
    }

    public void setSanPham(Product sanPham) {
        this.sanPham = sanPham;
        capNhatTongTien();
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
        capNhatTongTien();
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }
}
