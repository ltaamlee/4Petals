package fourpetals.com.repository;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.entity.ProductMaterialId;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, ProductMaterialId> {
	
	Collection<Product> findByMaNL(Material material);

	void deleteByMaSP_MaSP(Integer id);
}
