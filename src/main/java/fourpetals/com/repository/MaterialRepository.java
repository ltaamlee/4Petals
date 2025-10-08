package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
}