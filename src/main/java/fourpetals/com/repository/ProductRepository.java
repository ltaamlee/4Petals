package fourpetals.com.repository;

import fourpetals.com.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("select coalesce(sum(od.soLuong),0) from OrderDetail od where od.sanPham.maSP = :pId")
	Long sumSoldByProductId(@Param("pId") Integer productId);

	@Query("""
			    select p from Product p
			    where (:kw = '' or lower(p.tenSP) like lower(concat('%', :kw, '%')))
			      and (:status is null or p.trangThai = :status)
			      and (:categoryId is null or p.danhMuc.maDM = :categoryId)
			    order by p.maSP desc
			""")
	List<Product> searchNoPaging(@Param("kw") String kw, @Param("status") Integer status,
			@Param("categoryId") Integer categoryId);

	@Query("""
			    select p from Product p
			    where (:kw = '' or lower(p.tenSP) like lower(concat('%', :kw, '%')))
			      and (:status is null or p.trangThai = :status)
			      and (:categoryId is null or p.danhMuc.maDM = :categoryId)
			""")
	Page<Product> search(@Param("kw") String kw, @Param("status") Integer status,
			@Param("categoryId") Integer categoryId, Pageable pageable);

	Page<Product> findByTenSPContainingIgnoreCaseOrMoTaContainingIgnoreCase(String a, String b, Pageable pageable);

	boolean existsByTenSP(String tenSP);

	// 🔹 Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường / hoa in hoa)
	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> searchByName(@Param("keyword") String keyword);

	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> findByTenSPContainingIgnoreCase(@Param("keyword") String keyword);

	@Query("SELECT p FROM Product p WHERE p.danhMuc.maDM IN :categoryIds")
	List<Product> findByDanhMucIn(@Param("categoryIds") List<Integer> categoryIds);

	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.danhMuc.maDM IN :categoryIds")
	List<Product> findByTenSPContainingAndDanhMucIn(@Param("keyword") String keyword,
			@Param("categoryIds") List<Integer> categoryIds);

}
