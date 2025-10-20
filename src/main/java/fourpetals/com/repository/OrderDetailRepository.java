package fourpetals.com.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fourpetals.com.entity.OrderDetail;
import fourpetals.com.entity.OrderDetailId;
import fourpetals.com.enums.OrderStatus;
import java.util.List;
import fourpetals.com.entity.*;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
	@Query("select coalesce(sum(od.soLuong),0) from OrderDetail od where od.sanPham.maSP = :pId")
	Long sumSoldByProductId(@Param("pId") Integer productId);

	@Query("""
			    select od.id.maSP as productId, sum(coalesce(od.soLuong,0)) as totalSold
			    from OrderDetail od
			    where od.id.maSP in :ids
			    group by od.id.maSP
			""")
	List<Object[]> sumSoldRaw(@Param("ids") List<Integer> ids);

	// Helper: convert List<Object[]> -> Map<Integer, Long>
	default Map<Integer, Long> sumSoldByProductIds(List<Integer> ids) {
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (ids == null || ids.isEmpty())
			return map;

		List<Object[]> rows = sumSoldRaw(ids);
		for (Object[] row : rows) {
			Integer pid = (Integer) row[0];
			Long total = ((Number) row[1]).longValue();
			map.put(pid, total);
		}
		return map;
	}
}
