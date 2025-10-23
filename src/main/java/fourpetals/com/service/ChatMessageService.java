package fourpetals.com.service;

import java.util.List;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatRoom;
import fourpetals.com.entity.User;

public interface ChatMessageService {

    /** Lưu tin nhắn mới */
    ChatMessageResponse saveMessage(ChatMessageRequest request, User nguoiGui, User nguoiNhan, User nhanVienPhuTrach, ChatRoom chatRoom);

    /** Lấy tin nhắn trong phòng chat */
    List<ChatMessageResponse> getMessagesInRoom(String roomId);

    /** Lấy tin nhắn giữa hai người */
    List<ChatMessageResponse> getMessagesBetweenUsers(User user1, User user2);

    /** Lấy tin nhắn gửi bởi một người */
    List<ChatMessageResponse> getMessagesBySender(Integer userId);

    /** Lấy tin nhắn nhận bởi một người */
    List<ChatMessageResponse> getMessagesByReceiver(Integer userId);

    /** Lấy tin nhắn nội bộ giữa nhân viên */
    List<ChatMessageResponse> getInternalMessages();

    /** Lấy tin nhắn của khách hàng */
    List<ChatMessageResponse> getCustomerMessages();

    /** Lấy tin nhắn trong ca trực của nhân viên phụ trách */
    List<ChatMessageResponse> getMessagesByNhanVienPhuTrach(Integer nhanVienId);
}
