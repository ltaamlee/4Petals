package fourpetals.com.controller.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.service.ChatMessageService;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messagingTemplate;

	public ChatMessageController(ChatMessageService chatMessageService, SimpMessagingTemplate messagingTemplate) {
		super();
		this.chatMessageService = chatMessageService;
		this.messagingTemplate = messagingTemplate;
	}

	@PostMapping("/send")
	public ResponseEntity<ChatMessageResponse> sendMessage(@RequestBody ChatMessageRequest req) {
		ChatMessageResponse saved = chatMessageService.sendMessage(req);
		messagingTemplate.convertAndSend("/topic/chat", saved);
		return ResponseEntity.ok(saved);
	}

	@MessageMapping("/send")
	public void sendMessageWS(ChatMessageRequest req) {
		ChatMessageResponse saved = chatMessageService.sendMessage(req);
		messagingTemplate.convertAndSend("/topic/chat", saved);
	}

	@GetMapping
	public ResponseEntity<List<ChatMessageResponse>> getAllMessages() {
		return ResponseEntity.ok(chatMessageService.getAllMessages());
	}

	//Lấy tin nhắn theo user
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ChatMessageResponse>> getMessagesByUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(chatMessageService.getMessagesByUser(userId));
	}

	//Lấy tin nhắn nội bộ hoặc khách
	@GetMapping("/type/{isNoiBo}")
	public ResponseEntity<List<ChatMessageResponse>> getMessagesByType(@PathVariable boolean isNoiBo) {
		return ResponseEntity.ok(chatMessageService.getMessagesByType(isNoiBo));
	}
}
