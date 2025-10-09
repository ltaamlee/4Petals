// fourpetals/com/repository/OrderDetailRepository.java
package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.OrderDetail;
import fourpetals.com.entity.OrderDetailId;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {}
