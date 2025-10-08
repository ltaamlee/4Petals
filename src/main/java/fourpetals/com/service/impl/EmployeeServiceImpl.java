package fourpetals.com.service.impl;

import fourpetals.com.entity.Employee;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeServiceImpl(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Override public List<Employee> findAll() { return repo.findAll(); }
    @Override public Employee save(Employee e) { return repo.save(e); }
    @Override public boolean existsByEmail(String email) {
        return (email != null && !email.isBlank()) && repo.existsByEmail(email);
    }
}
