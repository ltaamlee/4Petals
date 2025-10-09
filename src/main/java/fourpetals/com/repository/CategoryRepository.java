package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
