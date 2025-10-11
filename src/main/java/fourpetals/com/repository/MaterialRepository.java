package fourpetals.com.repository;

import fourpetals.com.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findByTenNLContainingIgnoreCase(String q);
    Material findFirstByTenNLIgnoreCase(String ten);
}
