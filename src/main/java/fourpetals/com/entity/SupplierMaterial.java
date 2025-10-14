package fourpetals.com.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "NhaCungCapNguyenLieu")
public class SupplierMaterial implements Serializable {
	
	@EmbeddedId
	private SupplierMaterialId id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@MapsId("maNCC")
	@JoinColumn(name = "MaNCC")
	private Supplier nhaCungCap;

	@ManyToOne
	@MapsId("maNL")
	@JoinColumn(name = "MaNL")
	private Material nguyenLieu;

	public SupplierMaterialId getId() {
		return id;
	}

	public void setId(SupplierMaterialId id) {
		this.id = id;
	}

	public Supplier getNhaCungCap() {
		return nhaCungCap;
	}

	public void setNhaCungCap(Supplier nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}

	public Material getNguyenLieu() {
		return nguyenLieu;
	}

	public void setNguyenLieu(Material nguyenLieu) {
		this.nguyenLieu = nguyenLieu;
	}

	public SupplierMaterial() {
		super();
	}

	public SupplierMaterial(Supplier nhaCungCap, Material nguyenLieu) {
		super();
		this.nhaCungCap = nhaCungCap;
		this.nguyenLieu = nguyenLieu;
	}
}
