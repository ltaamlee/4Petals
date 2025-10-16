package fourpetals.com.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderDetailId implements Serializable {
    private Integer maDH;
    private Integer maSP;
	public Integer getMaDH() {
		return maDH;
	}
	
	public OrderDetailId() {
		super();
	}


	public void setMaDH(Integer maDH) {
		this.maDH = maDH;
	}
	public Integer getMaSP() {
		return maSP;
	}
	public void setMaSP(Integer maSP) {
		this.maSP = maSP;
	}
	public OrderDetailId(Integer maDH, Integer maSP) {
		super();
		this.maDH = maDH;
		this.maSP = maSP;
	}


	@Override
	public int hashCode() {
		return Objects.hash(maDH, maSP);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetailId other = (OrderDetailId) obj;
		return Objects.equals(maDH, other.maDH) && Objects.equals(maSP, other.maSP);
	}    
    
}
