package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
