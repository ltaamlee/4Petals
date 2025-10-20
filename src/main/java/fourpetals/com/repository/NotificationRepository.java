package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
	// Lấy tất cả notification cho 1 người dùng, theo thứ tự mới nhất
    List<Notification> findByNguoiNhanIdOrderByNgayTaoDesc(Integer nguoiNhanId);

    // Lấy các notification chưa đọc cho người dùng
    List<Notification> findByNguoiNhanIdAndDaDocFalseOrderByNgayTaoDesc(Integer nguoiNhanId);

}
