package fourpetals.com.dto.request.products;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProductMaterialLineRequest {
	@NotNull
	private Integer maNL;
	@NotNull
	@Min(1)
	private Integer soLuongCan;

	public Integer getMaNL() {
		return maNL;
	}

	public void setMaNL(Integer maNL) {
		this.maNL = maNL;
	}

	public Integer getSoLuongCan() {
		return soLuongCan;
	}

	public void setSoLuongCan(Integer soLuongCan) {
		this.soLuongCan = soLuongCan;
	}
}
