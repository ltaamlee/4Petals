package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fourpetals.com.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
