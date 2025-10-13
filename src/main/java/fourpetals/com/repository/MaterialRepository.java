package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.Material;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
	
	//Tìm kiếm
	
	List<Material> findByTenNLContainingIgnoreCase(String q);

	Material findFirstByTenNLIgnoreCase(String ten);
	
	//Kiểm tra tồn tại
	
	//Đếm số lượng
	
	//Xóa
	
	//Tìm kiếm nâng cao
}
