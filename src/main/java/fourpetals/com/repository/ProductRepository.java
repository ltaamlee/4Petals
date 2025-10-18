package fourpetals.com.repository;

import fourpetals.com.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("""
	        select p from Product p
	        where (:kw = '' or lower(p.tenSP) like lower(concat('%', :kw, '%')))
	          and (:status is null or p.trangThai = :status)
	          and (:categoryId is null or p.danhMuc.maDM = :categoryId)
	    """)
	    Page<Product> search(@Param("kw") String kw,
	                         @Param("status") Integer status,
	                         @Param("categoryId") Integer categoryId,
	                         Pageable pageable);
	
	Page<Product> findByTenSPContainingIgnoreCaseOrMoTaContainingIgnoreCase(String a, String b, Pageable pageable);

	boolean existsByTenSP(String tenSP);

}
