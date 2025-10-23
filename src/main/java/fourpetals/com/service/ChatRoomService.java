package fourpetals.com.service;

import java.util.List;

import fourpetals.com.entity.ChatRoom;
import fourpetals.com.enums.RoomType;

public interface ChatRoomService {
	// Tạo hoặc lấy phòng chat (nếu tồn tại)
	ChatRoom createOrGetRoom(String integer, RoomType loaiRoom, String moTa);

	// Lấy phòng theo roomId
	ChatRoom getRoomByRoomId(String roomId);

	// Lấy danh sách tất cả phòng
	List<ChatRoom> getAllRooms();

	// Lấy danh sách phòng theo loại
	List<ChatRoom> getRoomsByType(RoomType loaiRoom);

	// Xóa phòng
	void deleteRoom(String roomId);


}
