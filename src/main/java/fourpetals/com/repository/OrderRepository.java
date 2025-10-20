// fourpetals/com/repository/OrderRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
    
    List<Order> findByKhachHang(Customer khachHang);
    List<Order> findByKhachHangAndTrangThai(Customer khachHang, OrderStatus trangThai);


}
