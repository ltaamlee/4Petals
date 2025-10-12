package fourpetals.com.repository;

import fourpetals.com.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	boolean existsByEmail(String email);

// Tối ưu cho kiểm tra email trùng khi cập nhật
	boolean existsByEmailAndMaNVNot(String email, Integer maNV);

// ====== TÌM THEO TÊN ======
	List<Employee> findByHoTenContainingIgnoreCase(String hoTen);
}
