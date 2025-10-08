package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}