package fourpetals.com.service.impl;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.service.EmployeeService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public boolean existsBySdt(String sdt) {
		return employeeRepository.existsBySdt(sdt);
	}

	@Override
	public long countAll() {
		return employeeRepository.count();
	}
	
	@Override
	public Optional<Employee> findById(Integer maNV) {
	    return employeeRepository.findById(maNV);
	}
	
	@Override
	public Optional<Employee> findByUser(User user) {
	    if (user == null) return Optional.empty();
	    return employeeRepository.findByUser(user);
	}

}
