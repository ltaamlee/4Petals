package fourpetals.com.repository;

import fourpetals.com.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	//Kiểm tra số điện thoại
	boolean existsBySdt(String sdt);


// ====== TÌM THEO TÊN ======
	List<Employee> findByHoTenContainingIgnoreCase(String hoTen);
}
