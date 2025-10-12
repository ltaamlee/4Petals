package fourpetals.com.service;

import fourpetals.com.entity.Employee;
import java.util.List;

public interface EmployeeService {
	List<Employee> findAll();

	Employee findById(Integer id);

	Employee save(Employee e);

	boolean existsByEmail(String email);

	boolean existsByEmailExceptId(String email, Integer exceptId);

	List<Employee> searchByName(String name);
}