package fourpetals.com.service.impl;

import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public boolean existsBySdt(String sdt) {
    	return employeeRepository.existsBySdt(sdt);
    }

}
