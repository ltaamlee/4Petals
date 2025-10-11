package fourpetals.com.repository;

import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.entity.ProductMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, ProductMaterialId> {
    void deleteByProduct_MaSP(Integer maSP);
}
