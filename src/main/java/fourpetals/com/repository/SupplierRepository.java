package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
