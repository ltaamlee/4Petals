package fourpetals.com.service.impl;

import fourpetals.com.entity.Employee;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeServiceImpl(EmployeeRepository repo) {
        this.repo = repo;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findById(Integer id) {
        return repo.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public Employee save(Employee e) {
        return repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return repo.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailExceptId(String email, Integer exceptId) {
        if (email == null || email.isBlank()) return false;
        return repo.existsByEmailAndMaNVNot(email, exceptId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchByName(String name) {
        if (name == null || name.isBlank()) return repo.findAll();
        return repo.findByHoTenContainingIgnoreCase(name.trim());
    }
}
