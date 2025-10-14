package fourpetals.com.service.impl;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.CustomerService;
import fourpetals.com.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final UserService userService;

	public CustomerServiceImpl(CustomerRepository customerRepository, UserService userService) {
		this.customerRepository = customerRepository;
		this.userService = userService;
	}

	// ========================== KIỂM TRA ==========================

	@Override
	public boolean existsBySdt(String sdt) {
		return customerRepository.existsBySdt(sdt);
	}

	// ========================== ĐẾM ==========================

	@Override
	public long countAll() {
		return customerRepository.count();
	}

	@Override
	public long countByGioiTinh(Gender gioiTinh) {
		return customerRepository.countByGioiTinh(gioiTinh);
	}

	@Override
	public long countByHangThanhVien(CustomerRank hangThanhVien) {
		return customerRepository.countByHangThanhVien(hangThanhVien);
	}

	// ========================== CRUD ==========================

	@Override
	public Customer updateCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customerRepository.delete(customer);
	}

	// ========================== TÌM KIẾM ==========================

	@Override
	public Optional<Customer> findById(Integer maKH) {
		return customerRepository.findById(maKH);
	}

	@Override
	public Optional<Customer> findByUser(User user) {
		return customerRepository.findByUser(user);
	}

	@Override
	public Optional<Customer> findBySdt(String sdt) {
		return customerRepository.findBySdt(sdt);
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Page<Customer> searchCustomers(String keyword, Pageable pageable) {
	    return customerRepository.searchCustomers(keyword, pageable);
	}

	// ========================== ĐĂNG KÍ & LIÊN KẾT ==========================

	@Override
	public Customer registerCustomer(Customer customer, String roleName) {
		User user = customer.getUser();
		if (user != null) {
			userService.registerUser(user, roleName);
		}

		// Thiết lập hạng khách hàng mặc định khi mới đăng ký | tổng đơn hàng chưa đạt đến mức nâng hạng
		if (customer.getHangThanhVien() == null) {
			customer.setHangThanhVien(CustomerRank.THUONG);
		}

		return customerRepository.save(customer);
	}

	@Override
	public void linkUserWithCustomer(User user, Customer customer) {
		customer.setUser(user);
		customerRepository.save(customer);
	}

	// ========================== XẾP HẠNG / NGHIỆP VỤ ==========================

	@Override
	public CustomerRank getCustomerRank(Customer customer) {
		return customer.getHangThanhVien();
	}

}