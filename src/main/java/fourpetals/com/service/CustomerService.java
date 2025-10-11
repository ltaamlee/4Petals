
package fourpetals.com.service;

import fourpetals.com.entity.Customer;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.model.CustomerRank;

import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
	List<CustomerRowVM> list(CustomerRank filterRank, Month filterMonth, Integer year);
    List<CustomerRowVM> listWithSearch(String q, Month filterMonth, Integer year);
    CustomerRowVM oneRow(Integer id);

    Optional<Customer> findById(Integer id);
    Customer save(Customer c);

    CustomerStatsVM stats(Integer year, Month month);
}
