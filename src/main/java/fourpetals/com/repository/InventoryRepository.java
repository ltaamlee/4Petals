package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	@EntityGraph(attributePaths = {"nhaCungCap", "nhanVien"})
    List<Inventory> findAll();
}