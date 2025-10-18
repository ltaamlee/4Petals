package fourpetals.com.service;

import java.util.List;
import fourpetals.com.entity.Address;

public interface AddressService {
	
	List<Address> findByUsername(String username);

	Address getDefaultAddress(String username);

	Address save(Address address);

	void delete(Integer id);

	Address findById(Integer id);
}
