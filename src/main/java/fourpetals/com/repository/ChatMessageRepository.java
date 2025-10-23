package fourpetals.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fourpetals.com.entity.ChatMessage;
import fourpetals.com.entity.ChatRoom;
import fourpetals.com.entity.User;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

	// Lấy tất cả tin nhắn trong một phòng chat
	@Query("SELECT c FROM ChatMessage c WHERE c.chatRoom.roomId = :roomId ORDER BY c.thoiGianGui ASC")
	List<ChatMessage> findByChatRoomOrderByThoiGianGuiAsc(ChatRoom chatRoom);

	// Lấy tin nhắn giữa hai người (dù là khách hay nhân viên)

	@Query("""
			    SELECT c FROM ChatMessage c
			    WHERE
			        ((c.nguoiGui = :user1 AND c.nguoiNhan = :user2)
			         OR (c.nguoiGui = :user2 AND c.nguoiNhan = :user1))
			    ORDER BY c.thoiGianGui ASC
			""")
	List<ChatMessage> findMessagesBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

	// Lấy tin nhắn theo ID người gửi (nhân viên hoặc khách có tài khoản)
	@Query("""
			    SELECT c FROM ChatMessage c
			    WHERE c.nguoiGui.id = :userId
			    ORDER BY c.thoiGianGui ASC
			""")
	List<ChatMessage> findByNguoiGuiId(@Param("userId") Integer userId);

	// Lấy tin nhắn theo ID người nhận (nhân viên hoặc khách có tài khoản)
	@Query("""
			    SELECT c FROM ChatMessage c
			    WHERE c.nguoiNhan.id = :userId
			    ORDER BY c.thoiGianGui ASC
			""")
	List<ChatMessage> findByNguoiNhanId(@Param("userId") Integer userId);

	// Lấy tin nhắn nội bộ
	List<ChatMessage> findByNoiBoTrueOrderByThoiGianGuiAsc();

	/**
	 * 🔍 Lấy tin nhắn khách hàng
	 */
	List<ChatMessage> findByNoiBoFalseOrderByThoiGianGuiAsc();

	/**
	 * 🔍 Lấy tin nhắn trong ca trực của một nhân viên (hỗ trợ quản lý admin)
	 */
	@Query("""
			    SELECT c FROM ChatMessage c
			    WHERE c.nhanVienPhuTrach.id = :nhanVienId
			    ORDER BY c.thoiGianGui ASC
			""")
	List<ChatMessage> findByNhanVienPhuTrach(@Param("nhanVienId") Integer nhanVienId);

}
