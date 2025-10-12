package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.Material;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
	List<Material> findByTenNLContainingIgnoreCase(String q);

	Material findFirstByTenNLIgnoreCase(String ten);
}
