package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "PhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPN")
    private Integer maPN;

    @Column(name = "NgayNhap", nullable = false)
    private LocalDateTime ngayNhap = LocalDateTime.now();

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private Employee nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaNCC")
    private Supplier nhaCungCap;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryDetail> chiTietPhieuNhaps;
}
