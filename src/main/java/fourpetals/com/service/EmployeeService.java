package fourpetals.com.service;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
	// Kiểm tra tồn tại - SDT
	boolean existsBySdt(String sdt);

	// CRUD

	// Đếm
	long countAll();
	
    // ----------------- TÌM KIẾM -----------------
	Optional<Employee> findById(Integer maNV);
	Optional<Employee> findByUser(User user);

}