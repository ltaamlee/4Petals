// fourpetals/com/repository/OrderDetailRepository.java
package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.OrderDetail;
import fourpetals.com.entity.OrderDetailId;
import fourpetals.com.enums.OrderStatus;
import java.util.List;
import fourpetals.com.entity.*;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
}
