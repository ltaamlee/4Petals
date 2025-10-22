package fourpetals.com.repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
	// ===== ĐẾM SỐ LƯỢNG =====
	long countByGioiTinh(Gender gioiTinh);

	long countByHangThanhVien(CustomerRank hangThanhVien);

	// ===== TÌM KIẾM =====
	Optional<Customer> findById(Integer maKH);

	Optional<Customer> findByUser(User user);

	Optional<Customer> findBySdt(String sdt);

	// Đếm KH mới trong khoảng thời gian (u.createdAt là LocalDateTime)
	@Query("""
			SELECT COUNT(c) FROM Customer c
			LEFT JOIN c.user u
			WHERE u.createdAt >= :start AND u.createdAt < :end
			""")
	long countNewBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	// List + filter + phân trang
	@Query("""
			SELECT new fourpetals.com.model.CustomerRowVM(
			  c.maKH, c.hoTen, c.sdt, u.email, c.gioiTinh, c.hangThanhVien,
			  COALESCE(COUNT(DISTINCT o.maDH),0),
			  u.createdAt
			)
			FROM Customer c
			LEFT JOIN c.user u
			LEFT JOIN Order o ON o.khachHang = c
			WHERE (:keyword IS NULL OR :keyword = '' OR
			       LOWER(c.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			       LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			       c.sdt LIKE CONCAT('%', :keyword, '%'))
			  AND (:gender IS NULL OR c.gioiTinh = :gender)
			  AND (:rank IS NULL OR c.hangThanhVien = :rank)
			GROUP BY c.maKH, c.hoTen, c.sdt, u.email, c.gioiTinh, c.hangThanhVien, u.createdAt
			""")
	Page<fourpetals.com.model.CustomerRowVM> searchRows(@Param("keyword") String keyword,
			@Param("gender") Gender gender, @Param("rank") CustomerRank rank, Pageable pageable);

	// Chi tiết 1 khách
	@Query("""
			SELECT new fourpetals.com.dto.response.customers.CustomerDetailResponse(
			  c.maKH, c.hoTen, c.sdt, u.email, c.gioiTinh, c.hangThanhVien,
			  c.ngaySinh, c.diaChi,
			  u.createdAt,
			  COALESCE(COUNT(DISTINCT o.maDH),0)
			)
			FROM Customer c
			LEFT JOIN c.user u
			LEFT JOIN Order o ON o.khachHang = c
			WHERE c.maKH = :maKH
			GROUP BY c.maKH, c.hoTen, c.sdt, u.email, c.gioiTinh, c.hangThanhVien,
			         c.ngaySinh, c.diaChi, u.createdAt
			""")
	Optional<fourpetals.com.dto.response.customers.CustomerDetailResponse> getDetail(@Param("maKH") Integer maKH);

	// Đếm KH mới theo ngày trong khoảng — group by DATE(u.createdAt)
	@Query("""
			SELECT FUNCTION('DATE', u.createdAt) AS d, COUNT(c)
			FROM Customer c
			LEFT JOIN c.user u
			WHERE u.createdAt >= :start AND u.createdAt < :end
			GROUP BY FUNCTION('DATE', u.createdAt)
			ORDER BY d ASC
			""")
	List<Object[]> countNewByDayBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	Optional<Customer> findByUser_Username(String username);

	@Query("""
			SELECT CAST(c.user.createdAt AS string) AS day,
			       COUNT(c) AS total
			FROM Customer c
			WHERE c.user.createdAt BETWEEN :start AND :end
			GROUP BY CAST(c.user.createdAt AS string)
			ORDER BY c.user.createdAt
			""")
	List<Object[]> countNewCustomersByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}