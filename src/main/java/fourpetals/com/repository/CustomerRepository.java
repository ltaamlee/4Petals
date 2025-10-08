package fourpetals.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Optional<Customer> findByUser_Username(String username);
}
