package fourpetals.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	// ===== KIỂM TRA =====
	boolean existsBySdt(String sdt);

	// ===== ĐẾM SỐ LƯỢNG =====
	long countByGioiTinh(Gender gioiTinh);
	long countByHangThanhVien(CustomerRank hangThanhVien);

	// ===== TÌM KIẾM =====
	Optional<Customer> findById(Integer maKH);
	Optional<Customer> findByUser(User user);
	Optional<Customer> findBySdt(String sdt);

	@Query("""
			SELECT c FROM Customer c
			WHERE (:keyword IS NULL OR :keyword = ''
			    OR LOWER(c.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    OR LOWER(c.diaChi) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    OR LOWER(c.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')))
			""")
	Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);
}
