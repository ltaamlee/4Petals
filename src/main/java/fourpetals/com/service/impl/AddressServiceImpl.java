package fourpetals.com.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fourpetals.com.entity.Address;
import fourpetals.com.entity.Customer;
import fourpetals.com.repository.AddressRepository;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public List<Address> findByUsername(String username) {
        Customer customer = customerRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return addressRepo.findByKhachHang(customer);
    }

    @Override
    public Address getDefaultAddress(String username) {
        Customer customer = customerRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return addressRepo.findByKhachHangAndMacDinhTrue(customer);
    }

    @Override
    public Address save(Address address) {
        return addressRepo.save(address);
    }

    @Override
    public void delete(Integer id) {
        addressRepo.deleteById(id);
    }

    @Override
    public Address findById(Integer id) {
        return addressRepo.findById(id).orElse(null);
    }
}
