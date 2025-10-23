package fourpetals.com.repository;

import fourpetals.com.entity.Product;

import java.util.List;
import java.util.Optional;

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

	@EntityGraph(attributePaths = { "productMaterials", "productMaterials.maNL", "danhMuc" })
	@Query("SELECT p FROM Product p WHERE p.maSP = :id")
	Optional<Product> findByIdWithMaterials(@Param("id") Integer id);

	@EntityGraph(attributePaths = { "productMaterials", "productMaterials.maNL", "danhMuc" })
	@Query("SELECT DISTINCT p FROM Product p")
	List<Product> findAllWithMaterials();

	// 🔹 Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường / hoa in hoa)
	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> searchByName(@Param("keyword") String keyword);

	@EntityGraph(attributePaths = { "productMaterials", "productMaterials.maNL", "danhMuc" })
	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> findByTenSPContainingIgnoreCase(@Param("keyword") String keyword);

	@EntityGraph(attributePaths = { "productMaterials", "productMaterials.maNL", "danhMuc" })
	@Query("SELECT p FROM Product p WHERE p.danhMuc.maDM IN :categoryIds")
	List<Product> findByDanhMucIn(@Param("categoryIds") List<Integer> categoryIds);

	@EntityGraph(attributePaths = { "productMaterials", "productMaterials.maNL", "danhMuc" })
	@Query("SELECT p FROM Product p WHERE LOWER(p.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.danhMuc.maDM IN :categoryIds")
	List<Product> findByTenSPContainingAndDanhMucIn(@Param("keyword") String keyword,
			@Param("categoryIds") List<Integer> categoryIds);

	// 🔹 5 sản phẩm giảm giá nhiều nhất
	/*
	 * @Query("SELECT p FROM Product p WHERE p.trangThai = 1 ORDER BY (p.giaGoc - p.gia) / p.giaGoc DESC"
	 * ) List<Product> findTop5ByOrderByDiscountPercentDesc();
	 */
	// 🔹 10 sản phẩm bán chạy nhất
	@Query("SELECT p FROM Product p WHERE p.trangThai = 1 ORDER BY p.luotXem DESC")
	List<Product> findTop10ByOrderByViewCountDesc();

	List<Product> findTop5ByDanhMuc_MaDMAndMaSPNotOrderByMaSPDesc(Integer maDM, Integer maSP);

}
