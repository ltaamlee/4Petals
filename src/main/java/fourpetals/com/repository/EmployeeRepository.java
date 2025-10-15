package fourpetals.com.repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	//Kiểm tra số điện thoại
	boolean existsBySdt(String sdt);

	// ===== TÌM KIẾM =====
	Optional<Employee> findById(Integer maNV);
	Optional<Employee> findByUser(User user);

	

}
