//package fourpetals.com.controller.chat;
//
//import java.util.List;
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
////	
////	@PostMapping
////	String chat(@RequestBody ChatAIRequest request) {
////
////		return chatAIService.askAI(request);
////	}
//	
//	@PostMapping
//    public ResponseEntity<Map<String, Object>> askAIResponse(@RequestBody ChatAIRequest request) {
//        String reply;
//        try {
//            reply = chatAIService.askAI(request); // gọi service hiện tại của bạn
//        } catch (Exception e) {
//            reply = "Xin lỗi quý khách, ChatBox hiện không phản hồi được.";
//        }
//
//        Map<String,Object> resp = Map.of(
//            "candidates", List.of(
//                Map.of(
//                    "content", Map.of(
//                        "parts", List.of(Map.of("text", reply))
//                    )
//                )
//            )
//        );
//
//        return ResponseEntity.ok(resp);
//    }
//}
