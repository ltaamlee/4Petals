package fourpetals.com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DanhGia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDG")
    private Integer maDG;

    @Column(name = "DanhGia", nullable = false)
    private Integer danhGia; // số sao từ 1–5

    @Column(name = "BinhLuan", columnDefinition = "nvarchar(255)")
    private String binhLuan;

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    private Product sanPham; // Liên kết tới sản phẩm

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User nguoiDanhGia; // Người đánh giá (nếu bạn có bảng User)

    // ========== GETTER / SETTER ==========
    public Integer getMaDG() {
        return maDG;
    }

    public void setMaDG(Integer maDG) {
        this.maDG = maDG;
    }

    public Integer getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(Integer danhGia) {
        this.danhGia = danhGia;
    }

    public String getBinhLuan() {
        return binhLuan;
    }

    public void setBinhLuan(String binhLuan) {
        this.binhLuan = binhLuan;
    }

    public Product getSanPham() {
        return sanPham;
    }

    public void setSanPham(Product sanPham) {
        this.sanPham = sanPham;
    }

    public User getNguoiDanhGia() {
        return nguoiDanhGia;
    }

    public void setNguoiDanhGia(User nguoiDanhGia) {
        this.nguoiDanhGia = nguoiDanhGia;
    }
}
