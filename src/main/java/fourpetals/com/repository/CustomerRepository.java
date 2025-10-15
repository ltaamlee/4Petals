// fourpetals/com/repository/CustomerRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Customer;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    long countByGioiTinh(Gender gender);

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
    Page<fourpetals.com.model.CustomerRowVM> searchRows(
            @Param("keyword") String keyword,
            @Param("gender") Gender gender,
            @Param("rank") CustomerRank rank,
            Pageable pageable
    );

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
}
