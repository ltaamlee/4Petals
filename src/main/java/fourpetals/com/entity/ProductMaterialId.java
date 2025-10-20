package fourpetals.com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductMaterialId implements Serializable {
	@Column(name = "MaSP")
	private Integer maSP;

	@Column(name = "MaNL")
	private Integer maNL;

	public Integer getMaSP() {
		return maSP;
	}

	public void setMaSP(Integer maSP) {
		this.maSP = maSP;
	}

	public Integer getMaNL() {
		return maNL;
	}

	public void setMaNL(Integer maNL) {
		this.maNL = maNL;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNL, maSP);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductMaterialId other = (ProductMaterialId) obj;
		return Objects.equals(maNL, other.maNL) && Objects.equals(maSP, other.maSP);
	}
}