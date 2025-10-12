package fourpetals.com.repository;

import fourpetals.com.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByTenSPContainingIgnoreCaseOrMoTaContainingIgnoreCase(String a, String b, Pageable pageable);
}
