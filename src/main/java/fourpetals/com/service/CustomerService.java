package fourpetals.com.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;

import java.time.Month;
import java.util.List;

public interface CustomerService {

	// Kiểm tra số điện thoại
	boolean existsBySdt(String sdt);

	// Đếm số lượng
	long countAll();
	long countByGioiTinh(Gender gioiTinh);
    long countByHangThanhVien(CustomerRank hangThanhVien);

    //CRUD
	Customer updateCustomer(Customer customer);
	void deleteCustomer(Customer customer);
    
	// ===== TÌM KIẾM =====
	Optional<Customer> findById(Integer maKH);
	Optional<Customer> findByUser(User username);
	Optional<Customer> findBySdt(String sdt);

	List<Customer> findAll();
	Page<Customer> searchCustomers(String keyword, Pageable pageable);

	// ===== ĐĂNG KÍ LIÊN KẾT =====
	Customer registerCustomer(Customer customer, String roleName);
	void linkUserWithCustomer(User user, Customer customer);


	// ===== XẾP HẠNG / NGHIỆP VỤ =====
	CustomerRank getCustomerRank(Customer customer);

	Optional<Customer> findByUsername(String username);

	void updateAvatar(String username, String imageUrl);

	void save(Customer customer);

}