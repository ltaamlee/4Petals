package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Material;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
	
	//Kiểm tra tồn tại
	
	//Đếm số lượng
	
	//Xóa
	
	//Tìm kiếm nâng cao
}
