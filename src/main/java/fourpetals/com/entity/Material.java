package fourpetals.com.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NguyenLieu")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Material {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "MaNL")
	    private Integer maNL;

	    @Column(name = "TenNL", length = 100, nullable = false)
	    private String tenNL;

	    @Column(name = "SoLuongTon")
	    private Integer soLuongTon;

	    @Column(name = "DonViTinh", length = 50)
	    private String donViTinh;

	    @ManyToOne
	    @JoinColumn(name = "MaNCC", nullable = false)
	    private Supplier nhaCungCap;
}
