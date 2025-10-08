package fourpetals.com.service;

import java.util.Optional;

import fourpetals.com.entity.Customer;

public interface CustomerService {
	Optional<Customer> findByUser_Username(String username);
}
