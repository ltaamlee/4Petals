package fourpetals.com.controller.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatRoom;
import fourpetals.com.entity.User;
import fourpetals.com.enums.RoomType;
import fourpetals.com.mapper.ChatMessageMapper;
import fourpetals.com.service.ChatMessageService;
import fourpetals.com.service.ChatRoomService;
import fourpetals.com.service.UserService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatMessageService chatMessageService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private UserService userService;

	/**
	 * 📩 Nhận tin nhắn từ client WebSocket (/app/send)
	 */
	@MessageMapping("/send")
	public void receiveMessage(@Payload ChatMessageRequest request) {

		// 🔹 Lấy người gửi / người nhận nếu có
		// Nếu muốn trả về null khi không tìm thấy
		User nguoiGui = request.getNguoiGuiId() != null ? userService.findById(request.getNguoiGuiId()).orElse(null)
				: null;

		User nguoiNhan = request.getNguoiNhanId() != null ? userService.findById(request.getNguoiNhanId()).orElse(null)
				: null;

		// 🔹 Tạo hoặc lấy phòng chat
		ChatRoom room = chatRoomService
				.createOrGetRoom(request.getRoomId(),
						request.getNoiBo() != null && request.getNoiBo() ? RoomType.EMPLOYEE_INTERNAL
								: (nguoiGui == null ? RoomType.GUEST : RoomType.CUSTOMER),
						"Phòng chat " + request.getRoomId());

		// 🔹 Lưu tin nhắn
		ChatMessageResponse response = chatMessageService.saveMessage(request, nguoiGui, nguoiNhan, null, room);

		// 🔹 Xác định topic và gửi tin nhắn tới các client đang lắng nghe
		String topic = determineRoomTopic(request);
		messagingTemplate.convertAndSend(topic, response);
	}

	/**
	 * 📜 Lấy tất cả tin nhắn trong một phòng
	 */
	@GetMapping("/room/{roomId}")
	public ResponseEntity<List<ChatMessageResponse>> getMessagesByRoom(@PathVariable String roomId) {
		List<ChatMessageResponse> messages = chatMessageService.getMessagesInRoom(roomId);
		return ResponseEntity.ok(messages);
	}

	/**
	 * 🧭 Xác định topic gửi tin nhắn theo loại phòng
	 */
	private String determineRoomTopic(ChatMessageRequest request) {
	    if (Boolean.TRUE.equals(request.getNoiBo())) {
	        return "/topic/chat-internal";
	    } else if (request.getNguoiGuiId() == null) {
	        // Guest
	        return "/topic/chat-guest-" + request.getSessionId();
	    } else if (request.getNguoiNhanId() != null) {
	        return "/topic/chat-user-" + request.getNguoiNhanId();
	    } else {
	        return "/topic/chat-general";
	    }
	}

}
