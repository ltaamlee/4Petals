package fourpetals.com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fourpetals.com.dto.request.supplier.SupplierRequest;
import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;

public interface SupplierService {

    // CRUD cơ bản
    Supplier create(SupplierRequest request);
    Supplier update(SupplierRequest request);
    void delete(Integer maNCC);

    // Tìm kiếm
    Optional<Supplier> findById(Integer id);
    Optional<Supplier> findByTenNCC(String tenNCC);
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findBySdt(String sdt);

    // Tìm kiếm với nguyên liệu
    Optional<Supplier> findByIdWithMaterials(Integer maNCC);

    // Danh sách / phân trang / search (trả Entity)
    Page<Supplier> findAll(Pageable pageable);
    Page<Supplier> search(String keyword, Pageable pageable);
    Page<Supplier> findSuppliersByMaterial(Integer materialId, Pageable pageable);
    Page<Supplier> searchSuppliersByMaterial(Integer materialId, String keyword, Pageable pageable);

    // Thống kê
    long countTotalSuppliers();
    SupplierResponse getSupplierDetail(Integer maNCC);
	List<Supplier> findAll();

}
