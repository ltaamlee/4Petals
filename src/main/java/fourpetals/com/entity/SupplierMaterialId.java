package fourpetals.com.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class SupplierMaterialId implements Serializable {
	private Integer maNCC;
	private Integer maNL;

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public Integer getMaNL() {
		return maNL;
	}

	public void setMaNL(Integer maNL) {
		this.maNL = maNL;
	}

	public SupplierMaterialId() {
		super();
	}

	public SupplierMaterialId(Integer maNCC, Integer maNL) {
		super();
		this.maNCC = maNCC;
		this.maNL = maNL;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNCC, maNL);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupplierMaterialId other = (SupplierMaterialId) obj;
		return Objects.equals(maNCC, other.maNCC) && Objects.equals(maNL, other.maNL);
	}

}
