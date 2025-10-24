//package fourpetals.com.controller.chat;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import fourpetals.com.dto.chat.ChatAIRequest;
//import fourpetals.com.service.ChatAIService;
//
//@RestController
//@RequestMapping("/api/chat-ai")
//public class ChatAIController {
//
//	@Autowired
//	ChatAIService chatAIService;
//	
//	@PostMapping
//	String chat(@RequestBody ChatAIRequest request) {
//
//		return chatAIService.askAI(request);
//	}
//}
