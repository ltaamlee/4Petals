package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH")
    private Integer maDH;

    @Column(name = "NgayDat", nullable = false)
    private LocalDateTime ngayDat = LocalDateTime.now();

    @Column(name = "NgayGiao")
    private LocalDate ngayGiao;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 20)
    private OrderStatus trangThai = OrderStatus.CHO_XU_LY;

    @Column(name = "DiaChiGiao", length = 255)
    private String diaChiGiao;

    @Column(name = "SDTNguoiNhan", length = 15)
    private String sdtNguoiNhan;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "PhiVanChuyen", precision = 18, scale = 2)
    private BigDecimal phiVanChuyen = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "PhuongThucThanhToan", length = 50)
    private PaymentMethod phuongThucThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThaiThanhToan", length = 20)
    private PaymentStatus trangThaiThanhToan = PaymentStatus.CHUA_THANH_TOAN;

    @Column(name = "GhiChu", columnDefinition = "TEXT")
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private Customer khachHang;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private Employee nhanVien;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> chiTietDonHang;
}