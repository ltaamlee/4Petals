package fourpetals.com.service;

import java.util.List;

import fourpetals.com.entity.Notification;
import fourpetals.com.enums.NotificationType;

public interface NotificationService {
	// Tạo mới notification
    Notification createNotification(Integer nguoiNhanId, Integer donHangId, NotificationType loaiThongBao, String noiDung);

    // Lấy tất cả notification của người dùng
    List<Notification> getAllByUser(Integer nguoiNhanId);

    // Lấy các notification chưa đọc của người dùng
    List<Notification> getUnreadByUser(Integer nguoiNhanId);

    // Đánh dấu đã đọc notification
    void markAsRead(Integer notificationId);

    // Xóa notification
    void deleteNotification(Integer notificationId);
}
