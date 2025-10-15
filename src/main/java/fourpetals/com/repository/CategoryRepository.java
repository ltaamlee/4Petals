// fourpetals/com/repository/CategoryRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Category;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  @Query("""
    SELECT c FROM Category c
    WHERE (:kw IS NULL OR :kw = '' OR
           LOWER(c.tenDM) LIKE LOWER(CONCAT('%', :kw, '%')) OR
           LOWER(COALESCE(c.moTa, '')) LIKE LOWER(CONCAT('%', :kw, '%')))
    """)
  Page<Category> search(@Param("kw") String keyword, Pageable pageable);
}
