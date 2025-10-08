package fourpetals.com.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fourpetals.com.entity.Customer;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> findByUser_Username(String username) {
        return customerRepository.findByUser_Username(username);
    }
}
