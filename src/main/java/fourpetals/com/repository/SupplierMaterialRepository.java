package fourpetals.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;
import fourpetals.com.entity.SupplierMaterialId;

@Repository
public interface SupplierMaterialRepository extends JpaRepository<SupplierMaterial, SupplierMaterialId> {
       
    List<SupplierMaterial> findByNhaCungCapMaNCC(Integer maNCC);

	Optional<SupplierMaterial> findByNhaCungCap_MaNCCAndNguyenLieu_MaNL(Integer maNCC, Integer maNL);
	List<SupplierMaterial> findByNhaCungCap_MaNCC(Integer maNCC);
    List<SupplierMaterial> findByNguyenLieu_MaNL(Integer maNL);
    
    void deleteAllByNhaCungCap(Supplier nhaCungCap);

}
