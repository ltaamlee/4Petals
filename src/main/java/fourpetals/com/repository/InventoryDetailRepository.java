package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.InventoryDetail;

public interface InventoryDetailRepository extends JpaRepository<InventoryDetail, Integer> {
}
