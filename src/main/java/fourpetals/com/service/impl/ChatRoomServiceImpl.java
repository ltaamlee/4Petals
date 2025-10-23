package fourpetals.com.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.entity.ChatRoom;
import fourpetals.com.enums.RoomType;
import fourpetals.com.repository.ChatRoomRepository;
import fourpetals.com.service.ChatRoomService;

@Service
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom createOrGetRoom(String roomId, RoomType loaiRoom, String moTa) {
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("roomId không được null hoặc rỗng!");
        }
        return chatRoomRepository.findByRoomId(roomId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setRoomId(roomId);
                    room.setLoaiRoom(loaiRoom);
                    room.setMoTa(moTa);
                    return chatRoomRepository.save(room);
                });
    }


    @Override
    public ChatRoom getRoomByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng chat với roomId: " + roomId));
    }

    @Override
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public List<ChatRoom> getRoomsByType(RoomType loaiRoom) {
        return chatRoomRepository.findAll()
                .stream()
                .filter(room -> room.getLoaiRoom() == loaiRoom)
                .toList();
    }

    @Override
    public void deleteRoom(String roomId) {
        chatRoomRepository.findByRoomId(roomId)
                .ifPresent(chatRoomRepository::delete);
    }
}
