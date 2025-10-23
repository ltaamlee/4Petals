package fourpetals.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fourpetals.com.entity.ChatRoom;

import fourpetals.com.enums.RoomType;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
	Optional<ChatRoom> findByRoomId(String roomId);

	boolean existsByRoomId(String roomId);

	// Lấy theo loại phòng: GUEST, CUSTOMER, EMPLOYEE_INTERNAL
	Optional<ChatRoom> findByLoaiRoom(RoomType loaiRoom);
}