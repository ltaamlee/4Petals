package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.entity.ProductMaterialId;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, ProductMaterialId> {
	void deleteByMaSP_MaSP(Integer maSP);
}
