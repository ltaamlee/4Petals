// fourpetals/com/service/CustomerService.java
package fourpetals.com.service;

import fourpetals.com.dto.response.customers.CustomerDetailResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.Series;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
	Page<CustomerRowVM> searchRows(String keyword, Gender gender, CustomerRank rank, int page, int size);

	Optional<CustomerDetailResponse> getDetail(Integer maKH);

	long countNewInCurrentMonth();

	long countByGender(Gender gender);

	// Đếm số lượng
	long countAll();

	long countByGioiTinh(Gender gioiTinh);

	long countByHangThanhVien(CustomerRank hangThanhVien);

	// CRUD
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

	Customer save(Customer customer);

	Series getNewCustomersSeries(String range, LocalDate start, LocalDate end);

	void updateRank(Integer maKH, CustomerRank rank);

}