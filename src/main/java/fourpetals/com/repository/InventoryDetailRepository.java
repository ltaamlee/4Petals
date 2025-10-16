package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;

public interface InventoryDetailRepository extends JpaRepository<InventoryDetail, Integer> {

	@Query("SELECT ct FROM InventoryDetail ct " + "JOIN FETCH ct.phieuNhap pn " + "JOIN FETCH ct.nguyenLieu nl "
			+ "ORDER BY pn.maPN ASC")
	List<InventoryDetail> findAllWithMaterialAndInventory();

	List<InventoryDetail> findByPhieuNhap(Inventory phieuNhap);

	@Query("SELECT ct FROM InventoryDetail ct " + "JOIN FETCH ct.nguyenLieu nl " + "JOIN FETCH ct.phieuNhap pn "
			+ "WHERE pn.maPN = :maPN")
	List<InventoryDetail> findByPhieuNhapWithMaterial(Integer maPN);

}
