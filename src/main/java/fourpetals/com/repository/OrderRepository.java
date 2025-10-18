// fourpetals/com/repository/OrderRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	long countByKhachHang_MaKH(Integer maKH);

	@Query("""
			    select o.khachHang.maKH, count(o)
			    from Order o
			    where o.khachHang.maKH in :ids
			    group by o.khachHang.maKH
			""")
	List<Object[]> countByCustomerIds(@Param("ids") List<Integer> customerIds);

	@Query("""
			    select o.khachHang.maKH, max(o.ngayDat)
			    from Order o
			    where o.khachHang.maKH in :ids
			    group by o.khachHang.maKH
			""")
	List<Object[]> lastOrderAtByCustomerIds(@Param("ids") List<Integer> customerIds);

	@Query("""
			    select o.khachHang.maKH, coalesce(sum(o.tongTien), 0)
			    from Order o
			    where o.khachHang.maKH in :ids
			    group by o.khachHang.maKH
			""")
	List<Object[]> totalSpentByCustomerIds(@Param("ids") List<Integer> customerIds);

	// Tuỳ chọn: đếm theo trạng thái (nếu cần lọc)
	@Query("""
			    select o.khachHang.maKH, count(o)
			    from Order o
			    where o.khachHang.maKH in :ids and o.trangThai = :status
			    group by o.khachHang.maKH
			""")
	List<Object[]> countByCustomerIdsAndStatus(@Param("ids") List<Integer> customerIds,
			@Param("status") OrderStatus status);

	// Tuỳ chọn: đếm số đơn trong khoảng thời gian (cho dashboard)
	@Query("""
			    select o.khachHang.maKH, count(o)
			    from Order o
			    where o.khachHang.maKH in :ids
			      and o.ngayDat between :start and :end
			    group by o.khachHang.maKH
			""")
	List<Object[]> countByCustomerIdsInRange(@Param("ids") List<Integer> customerIds,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	List<Order> findByKhachHang(Customer customer);

    // Đếm đơn hàng theo trạng thái
    long countByTrangThai(OrderStatus trangThai);

    // Đếm đơn hàng theo khoảng thời gian
    long countByNgayDatBetween(LocalDateTime from, LocalDateTime to);
    

    // Lấy danh sách chi tiết đơn hàng của từng dòng cụ thể
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.chiTietDonHang " +
            "LEFT JOIN FETCH o.nhanVien " +
            "LEFT JOIN FETCH o.khachHang " +
            "WHERE o.maDH = :id")
     Optional<Order> findByIdWithDetails(Integer id);

}
