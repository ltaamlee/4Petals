package fourpetals.com.service;

import java.util.List;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;

public interface ChatMessageService {
	// Gửi tin nhắn (nội bộ hoặc khách)
	ChatMessageResponse sendMessage(ChatMessageRequest request);

	// Lấy tất cả tin nhắn
	List<ChatMessageResponse> getAllMessages();

	// Lấy tin nhắn theo 1 nhân viên
	List<ChatMessageResponse> getMessagesByUser(Integer userId);

	// Lấy tin nhắn nội bộ hoặc khách
	List<ChatMessageResponse> getMessagesByType(boolean isNoiBo);
}
