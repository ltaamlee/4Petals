package fourpetals.com.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fourpetals.com.entity.Customer;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
    private CustomerRepository customerRepository;

	@Autowired
    private UserRepository userRepository;
	
    @Override
    public Optional<Customer> findByUser_Username(String username) {
        return customerRepository.findByUser_Username(username);
    }
    @Override
    public void updateAvatar(String username, String imageUrl) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setImageUrl(imageUrl);
            userRepository.save(user);
        });
    }
}
