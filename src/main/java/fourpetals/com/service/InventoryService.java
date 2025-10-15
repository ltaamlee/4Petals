package fourpetals.com.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;

public interface InventoryService {

    // -------------------------
    // CRUD Inventory
    // -------------------------
    List<Inventory> list(String keyword, Pageable pageable);

    Optional<Inventory> findById(Integer id);

    Inventory get(Integer id);

    Inventory create(Inventory inventory);

    Inventory update(Integer id, Inventory inventory);

    void delete(Integer id);

    // -------------------------
    // Tìm kiếm nâng cao
    // -------------------------
    List<Inventory> findByEmployee(Employee employee);

    List<Inventory> findBySupplier(Supplier supplier);

    List<Inventory> findByDateRange(LocalDate from, LocalDate to);

    Page<Inventory> searchInventories(String keyword, LocalDate from, LocalDate to, Pageable pageable);

    // -------------------------
    // Kiểm tra tồn tại
    // -------------------------
    boolean existsById(Integer id);

    boolean existsBySupplier(Supplier supplier);

    // -------------------------
    // Đếm
    // -------------------------
    long countAllInventories();

    long countBySupplier(Supplier supplier);

    long countByEmployee(Employee employee);

    // -------------------------
    // Quản lý InventoryDetail
    // -------------------------
    List<InventoryDetail> getDetailsByInventory(Inventory inventory);

    List<InventoryDetail> getDetailsByInventoryId(Integer inventoryId);

    void addMaterialToInventory(Inventory inventory, Material material, Integer quantity, BigDecimal giaNhap);

    void removeMaterialFromInventory(Inventory inventory, Material material);

    void updateInventoryDetailQuantity(Inventory inventory, Material material, Integer quantity, BigDecimal giaNhap);

    BigDecimal calculateTotalAmount(Inventory inventory);

    // -------------------------
    // Xuất/nhập dữ liệu
    // -------------------------
    void exportInventoriesToExcel(OutputStream os);

    void importInventoriesFromExcel(MultipartFile file);
}
