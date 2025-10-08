package fourpetals.com.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietPhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTPN")
    private Integer maCTPN;

    @ManyToOne
    @JoinColumn(name = "MaPN")
    private Inventory phieuNhap;

    @ManyToOne
    @JoinColumn(name = "MaNL")
    private Material nguyenLieu;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "GiaNhap", precision = 18, scale = 2, nullable = false)
    private BigDecimal giaNhap;

    @Column(name = "ThanhTien", precision = 18, scale = 2)
    private BigDecimal thanhTien;

    @PrePersist
    @PreUpdate
    private void tinhThanhTien() {
        if (soLuong != null && giaNhap != null)
            thanhTien = giaNhap.multiply(BigDecimal.valueOf(soLuong));
    }
}
