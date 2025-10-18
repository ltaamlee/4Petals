// fourpetals/com/repository/OrderDetailRepository.java
package fourpetals.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fourpetals.com.entity.Order;
import fourpetals.com.entity.OrderDetail;
import fourpetals.com.entity.OrderDetailId;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

}
