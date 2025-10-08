
package fourpetals.com.service;

import fourpetals.com.entity.Customer;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerRank;
import fourpetals.com.model.CustomerStatsVM;

import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerRowVM> list(CustomerRank filterRank, Month filterMonth, Integer year);
    CustomerStatsVM stats(Integer year, Month filterMonth);
    Optional<Customer> findById(Integer id);
    Customer save(Customer c);
    void deleteById(Integer id);
}
