package fourpetals.com.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.repository.InventoryRepository;
import fourpetals.com.service.InventoryService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService{

	@Autowired
	private InventoryRepository inventoryRepository;
	//THÊM TẤT CẢ CÁC PHƯƠNG THỨC TỪ INVENTORY SERVICE VÀO 
	@Override
	public List<Inventory> findAll(){
		return inventoryRepository.findAll();
	}
	
	//VIẾT LẠI TẤT CÁC CÁC PHƯƠNG THỨC Ở DƯỚI
	
	@Override
	public List<Inventory> list(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional<Inventory> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	@Override
	public Inventory get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Inventory create(Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Inventory update(Integer id, Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Inventory> findByEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Inventory> findBySupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Inventory> findByDateRange(LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<Inventory> searchInventories(String keyword, LocalDate from, LocalDate to, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean existsBySupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public long countAllInventories() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long countBySupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long countByEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<InventoryDetail> getDetailsByInventory(Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<InventoryDetail> getDetailsByInventoryId(Integer inventoryId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addMaterialToInventory(Inventory inventory, Material material, Integer quantity, BigDecimal giaNhap) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeMaterialFromInventory(Inventory inventory, Material material) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateInventoryDetailQuantity(Inventory inventory, Material material, Integer quantity,
			BigDecimal giaNhap) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public BigDecimal calculateTotalAmount(Inventory inventory) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void exportInventoriesToExcel(OutputStream os) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void importInventoriesFromExcel(MultipartFile file) {
		// TODO Auto-generated method stub
		
	}
	
}
