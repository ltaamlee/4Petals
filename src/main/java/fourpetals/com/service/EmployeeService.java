package fourpetals.com.service;

import fourpetals.com.entity.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee save(Employee e);
    boolean existsByEmail(String email);
}
