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

	public Integer getMaCTPN() {
		return maCTPN;
	}

	public void setMaCTPN(Integer maCTPN) {
		this.maCTPN = maCTPN;
	}

	public Inventory getPhieuNhap() {
		return phieuNhap;
	}

	public void setPhieuNhap(Inventory phieuNhap) {
		this.phieuNhap = phieuNhap;
	}

	public Material getNguyenLieu() {
		return nguyenLieu;
	}

	public void setNguyenLieu(Material nguyenLieu) {
		this.nguyenLieu = nguyenLieu;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public BigDecimal getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(BigDecimal giaNhap) {
		this.giaNhap = giaNhap;
	}

	public BigDecimal getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(BigDecimal thanhTien) {
		this.thanhTien = thanhTien;
	}
    
    
}
