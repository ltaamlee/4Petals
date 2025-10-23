package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Material;


@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
	
	@Query("SELECT COUNT(maNL) FROM Material maNL")
    long countAllMaterial();
}
