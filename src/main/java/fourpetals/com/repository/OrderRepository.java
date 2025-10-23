package fourpetals.com.repository;

import fourpetals.com.dto.response.dashboard.RecentOrderResponse;
import fourpetals.com.dto.response.dashboard.TopProductResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	// NEW – không đụng hàm cũ
	@Query("""
			  SELECT o FROM Order o
			  WHERE o.trangThai = :closed
			    AND ( :kw IS NULL OR :kw = ''
			          OR LOWER(o.khachHang.hoTen) LIKE LOWER(CONCAT('%', :kw, '%'))
			          OR LOWER(o.sdtNguoiNhan)    LIKE LOWER(CONCAT('%', :kw, '%'))
			          OR LOWER(o.diaChiGiao)      LIKE LOWER(CONCAT('%', :kw, '%'))
			        )
			  ORDER BY o.ngayDat DESC
			""")
	Page<Order> findClosedOrders(@Param("kw") String kw, @Param("closed") fourpetals.com.enums.OrderStatus closed,
			Pageable pageable);

	@Query("select coalesce(sum(od.soLuong),0) from OrderDetail od where od.sanPham.maSP = :pId")
	Long sumSoldByProductId(@Param("pId") Integer productId);

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

	List<Order> findByKhachHang(Customer khachHang);

	List<Order> findByKhachHangAndTrangThai(Customer khachHang, OrderStatus trangThai);

	// Đếm đơn hàng theo trạng thái
	long countByTrangThai(OrderStatus trangThai);

	// Đếm đơn hàng theo khoảng thời gian
	long countByNgayDatBetween(LocalDateTime from, LocalDateTime to);

	// Lấy danh sách chi tiết đơn hàng của từng dòng cụ thể với từng role nhân viên
	@Query("""
			SELECT DISTINCT o FROM Order o
			LEFT JOIN FETCH o.chiTietDonHang
			LEFT JOIN FETCH o.nhanVienDuyet
			LEFT JOIN FETCH o.nhanVienDongGoi
			LEFT JOIN FETCH o.nhanVienGiaoHang
			LEFT JOIN FETCH o.khachHang
			WHERE o.maDH = :id
			""")
	Optional<Order> findByIdWithDetails(Integer id);

	// (Phục vụ thống kê) — số KH mới theo từng tháng trong một năm
	// KH mới = KH có đơn đầu tiên nằm trong tháng đó
	@Query("""
			    select month(min(o.ngayDat)) as m, count(distinct o.khachHang.maKH) as cnt
			    from Order o
			    where year(o.ngayDat) = :year
			    group by o.khachHang.maKH
			""")
	List<Object[]> firstOrderMonthPerCustomer(@Param("year") int year);

	// Trong OrderRepository.java
	@Query("SELECT o FROM Order o WHERE o.trangThai = fourpetals.com.enums.OrderStatus.DA_XAC_NHAN OR o.trangThai = fourpetals.com.enums.OrderStatus.DA_DONG_DON")
	List<Order> findAllConfirmedOrders();

	@Query("SELECT o FROM Order o WHERE o.trangThai = fourpetals.com.enums.OrderStatus.DANG_GIAO")
	List<Order> findAllDeliveringOrders();

	// Tìm kiếm + Lọc
	@Query("SELECT o FROM Order o " + "WHERE (:trangThai IS NULL OR o.trangThai = :trangThai) "
			+ "AND (:keyword IS NULL OR " + "LOWER(o.khachHang.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(o.phuongThucThanhToan) LIKE LOWER(CONCAT('%', :keyword, '%'))" + ")")
	Page<Order> filterOrders(@Param("trangThai") OrderStatus trangThai, @Param("keyword") String keyword,
			Pageable pageable);

	// SHIPPER LẤY ĐƠN GIAO THEO PHÂN CÔNG
	@Query("""
			    SELECT o
			    FROM Order o
			    WHERE o.nhanVienGiaoHang.maNV = :shipperId
			    ORDER BY o.ngayDat DESC
			""")
	List<Order> findOrdersAssignedToShipper(@Param("shipperId") Integer shipperId);

	@Query("""
			    SELECT DISTINCT o
			    FROM Order o
			    LEFT JOIN FETCH o.chiTietDonHang
			    LEFT JOIN FETCH o.khachHang
			    WHERE o.nhanVienGiaoHang.maNV = :shipperId
			""")
	List<Order> findOrdersWithDetailsForShipper(@Param("shipperId") Integer shipperId);

	// 1️⃣ Tổng doanh thu (loại bỏ trạng thái HỦY)
	@Query("""
			    SELECT COALESCE(SUM(o.tongTien), 0)
			    FROM Order o
			    WHERE o.trangThai <> :excludeStatus
			""")
	BigDecimal sumRevenueExcludeStatus(@Param("excludeStatus") OrderStatus huy);

	// 2️⃣ Sản phẩm bán chạy (top N) — JPQL không hỗ trợ LIMIT trực tiếp, dùng
	// Pageable
	@Query("""
			SELECT new fourpetals.com.dto.response.dashboard.TopProductResponse(
			    od.sanPham.maSP,
			    od.sanPham.tenSP,
			    SUM(od.soLuong * od.giaBan),
			    SUM(od.soLuong)
			)
			FROM OrderDetail od
			WHERE od.donHang.ngayDat BETWEEN :start AND :end
			GROUP BY od.sanPham.maSP, od.sanPham.tenSP
			ORDER BY SUM(od.soLuong * od.giaBan) DESC
			""")
	List<TopProductResponse> findTopProducts(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
			Pageable pageable);

	// 3️⃣ Đơn hàng gần đây (top N) — dùng Pageable
	@Query("""
			SELECT new fourpetals.com.dto.response.dashboard.RecentOrderResponse(
			    o.maDH,
			    o.khachHang.hoTen,
			    o.trangThai,
			    o.ngayDat
			)
			FROM Order o
			ORDER BY o.ngayDat DESC
			""")
	List<RecentOrderResponse> findRecentOrders(Pageable pageable);

	// 4️⃣ Doanh thu theo ngày (chart) — JPQL trả List<Object[]>
	@Query("""
			SELECT FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d') AS label,
			       SUM(o.tongTien) AS value
			FROM Order o
			WHERE o.ngayDat BETWEEN :start AND :end
			  AND o.trangThai <> :excludeStatus
			GROUP BY FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d')
			ORDER BY FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d')
			""")
	List<Object[]> sumRevenueByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
			@Param("excludeStatus") OrderStatus excludeStatus);

	// 5️⃣ Đếm đơn hàng theo ngày (chart)
	@Query("""
			SELECT FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d') AS label,
			       COUNT(o) AS value
			FROM Order o
			WHERE o.ngayDat BETWEEN :start AND :end
			GROUP BY FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d')
			ORDER BY FUNCTION('DATE_FORMAT', o.ngayDat, '%Y-%m-%d')
			""")
	List<Object[]> countOrdersByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


	List<Order> findByKhachHangOrderByNgayDatDesc(Customer khachHang);

	List<Order> findByKhachHangAndTrangThaiOrderByNgayDatDesc(Customer khachHang, OrderStatus trangThai);

	@Query("SELECT o FROM Order o WHERE o.nhanVienGiaoHang.maNV = :maNV")
    List<Order> findAllOrdersByShipperMaNV(@Param("maNV") Integer maNV);

}