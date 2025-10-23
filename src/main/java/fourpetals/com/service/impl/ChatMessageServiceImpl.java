package fourpetals.com.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatMessage;
import fourpetals.com.entity.ChatRoom;
import fourpetals.com.entity.User;
import fourpetals.com.mapper.ChatMessageMapper;
import fourpetals.com.repository.ChatMessageRepository;
import fourpetals.com.service.ChatMessageService;

@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /** Lưu tin nhắn mới */
    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request, User nguoiGui, User nguoiNhan, User nhanVienPhuTrach, ChatRoom chatRoom) {
        ChatMessage message = ChatMessageMapper.toEntity(request, nguoiGui, nguoiNhan, nhanVienPhuTrach, chatRoom);
        message.setThoiGianGui(LocalDateTime.now());
        ChatMessage saved = chatMessageRepository.save(message);
        return ChatMessageMapper.toResponse(saved);
    }

    /** Lấy tin nhắn trong phòng */
    @Override
    public List<ChatMessageResponse> getMessagesInRoom(String roomId) {
        ChatRoom room = new ChatRoom();
        room.setRoomId(roomId);
        return chatMessageRepository.findByChatRoomOrderByThoiGianGuiAsc(room)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }
 

    /** Lấy tin nhắn giữa hai người */
    @Override
    public List<ChatMessageResponse> getMessagesBetweenUsers(User user1, User user2) {
        return chatMessageRepository.findMessagesBetweenUsers(user1, user2)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Lấy tin nhắn gửi bởi người dùng */
    @Override
    public List<ChatMessageResponse> getMessagesBySender(Integer userId) {
        return chatMessageRepository.findByNguoiGuiId(userId)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Lấy tin nhắn nhận bởi người dùng */
    @Override
    public List<ChatMessageResponse> getMessagesByReceiver(Integer userId) {
        return chatMessageRepository.findByNguoiNhanId(userId)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Lấy tin nhắn nội bộ */
    @Override
    public List<ChatMessageResponse> getInternalMessages() {
        return chatMessageRepository.findByNoiBoTrueOrderByThoiGianGuiAsc()
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Lấy tin nhắn khách hàng */
    @Override
    public List<ChatMessageResponse> getCustomerMessages() {
        return chatMessageRepository.findByNoiBoFalseOrderByThoiGianGuiAsc()
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Lấy tin nhắn trong ca trực */
    @Override
    public List<ChatMessageResponse> getMessagesByNhanVienPhuTrach(Integer nhanVienId) {
        return chatMessageRepository.findByNhanVienPhuTrach(nhanVienId)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }
}
