package fourpetals.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    // Kiểm tra trùng Email - SDT
    boolean existsByEmail(String email);
    boolean existsBySdt(String sdt);
    // Tìm kiếm cơ bản
    Optional<Supplier> findByTenNCC(String tenNCC);
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findBySdt(String sdt);

    // ------------------- Danh sách / tìm kiếm (không load nguyên liệu) -------------------
    @Query("SELECT s FROM Supplier s " +
           "WHERE (:keyword IS NULL OR LOWER(s.tenNCC) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.diaChi) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY s.createdAt DESC")
    Page<Supplier> searchSuppliers(@Param("keyword") String keyword, Pageable pageable);

    // ------------------- View chi tiết (load nguyên liệu + material) -------------------
    @EntityGraph(attributePaths = {"nhaCungCapNguyenLieu", "nhaCungCapNguyenLieu.nguyenLieu"})
    @Query("SELECT s FROM Supplier s WHERE s.maNCC = :maNCC")
    Optional<Supplier> findByIdWithMaterials(@Param("maNCC") Integer maNCC);


    // ------------------- Lọc theo nguyên liệu (không load nguyên liệu) -------------------
    @Query("SELECT DISTINCT s FROM Supplier s " +
           "JOIN s.nhaCungCapNguyenLieu sm " +
           "WHERE sm.nguyenLieu.maNL = :maNL")
    Page<Supplier> findSuppliersByMaterial(@Param("maNL") Integer maNL, Pageable pageable);

    // Lọc theo nguyên liệu + từ khóa
    @Query("SELECT DISTINCT s FROM Supplier s " +
           "JOIN s.nhaCungCapNguyenLieu sm " +
           "WHERE sm.nguyenLieu.maNL = :maNL " +
           "AND (:keyword IS NULL OR LOWER(s.tenNCC) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Supplier> searchSuppliersByMaterial(@Param("maNL") Integer maNL,
                                             @Param("keyword") String keyword,
                                             Pageable pageable);

    // ------------------- Đếm tổng -------------------
    @Query("SELECT COUNT(s) FROM Supplier s")
    long countTotalSuppliers();
}
