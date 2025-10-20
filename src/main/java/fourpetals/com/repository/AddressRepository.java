package fourpetals.com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fourpetals.com.entity.Address;
import fourpetals.com.entity.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	List<Address> findByKhachHang(Customer khachHang);

	Address findByKhachHangAndMacDinhTrue(Customer khachHang);
}
