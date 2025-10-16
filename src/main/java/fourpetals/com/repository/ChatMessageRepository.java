package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import fourpetals.com.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
	// Lấy tất cả tin nhắn của 1 nhân viên
	List<ChatMessage> findByNguoiGuiUserIdOrNguoiNhanUserId(Integer nguoiGuiId, Integer nguoiNhanId);

	// Lấy tin nhắn theo nhân viên phụ trách ca trực
	List<ChatMessage> findByNhanVienPhuTrachUserId(Integer nhanVienPhuTrachId);

	// Lọc tin nhắn nội bộ hoặc khách
	List<ChatMessage> findByIsNoiBo(boolean isNoiBo);

	// Lọc theo khoảng thời gian
	List<ChatMessage> findByTimestampBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}
