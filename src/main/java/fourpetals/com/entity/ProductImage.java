package fourpetals.com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HinhAnhSanPham")
public class ProductImage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHA")
    private Integer maHA;

    @Column(name = "DuongDan", length = 255, nullable = false)
    private String duongDan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSP")
    private Product sanPham;

    
	public ProductImage() {
		super();
	}

	public ProductImage(Integer maHA, String duongDan, Product sanPham) {
		super();
		this.maHA = maHA;
		this.duongDan = duongDan;
		this.sanPham = sanPham;
	}

	public Integer getMaHA() {
		return maHA;
	}

	public void setMaHA(Integer maHA) {
		this.maHA = maHA;
	}

	public String getDuongDan() {
		return duongDan;
	}

	public void setDuongDan(String duongDan) {
		this.duongDan = duongDan;
	}

	public Product getSanPham() {
		return sanPham;
	}

	public void setSanPham(Product sanPham) {
		this.sanPham = sanPham;
	}
    
    
}
