package fourpetals.com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import fourpetals.com.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

  @Query("""
    SELECT p FROM Product p
    WHERE (:q IS NULL OR LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :q, '%')))
      AND (:catId IS NULL OR p.danhMuc.maDM = :catId)
      AND (:supId IS NULL OR p.nhaCungCap.maNCC = :supId)
    ORDER BY p.maSP DESC
  """)
  List<Product> search(String q, Integer catId, Integer supId);

  @Query("SELECT COUNT(p) FROM Product p")
  long total();
}
