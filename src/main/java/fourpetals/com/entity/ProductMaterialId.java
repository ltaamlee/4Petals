package fourpetals.com.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductMaterialId implements Serializable {
	private Integer productId;
	private Integer materialId;

	public ProductMaterialId() {
	}

	public ProductMaterialId(Integer productId, Integer materialId) {
		this.productId = productId;
		this.materialId = materialId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ProductMaterialId))
			return false;
		ProductMaterialId that = (ProductMaterialId) o;
		return Objects.equals(productId, that.productId) && Objects.equals(materialId, that.materialId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, materialId);
	}
}
