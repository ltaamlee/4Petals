package fourpetals.com.service;

import java.util.Optional;

import fourpetals.com.entity.Customer;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;

import java.time.Month;
import java.util.List;

public interface CustomerService {
	Optional<Customer> findByUser_Username(String username);

	void updateAvatar(String username, String imageUrl);

	List<CustomerRowVM> list(CustomerRank filterRank, Month filterMonth, Integer year);

	List<CustomerRowVM> listWithSearch(String q, Month filterMonth, Integer year);

	CustomerRowVM oneRow(Integer id);

	Optional<Customer> findById(Integer id);

	Customer save(Customer c);

	CustomerStatsVM stats(Integer year, Month month);
}
