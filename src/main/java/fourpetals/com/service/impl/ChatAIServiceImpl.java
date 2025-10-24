//package fourpetals.com.service.impl;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import fourpetals.com.dto.chat.ChatAIRequest;
//import fourpetals.com.service.ChatAIService;
//import jakarta.annotation.PostConstruct;
//
//@Service
//public class ChatAIServiceImpl implements ChatAIService{
//
//	
//	
//	@Value("${OPENAI_KEY}")
//    private String openaiKey;
//
//	@PostConstruct
//	public void init() {
//	    System.out.println("OPENAI_KEY = " + openaiKey);
//	}
//
//	
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public String askAI(ChatAIRequest request) {
//        try {
//            // payload cho OpenAI
//            Map<String, Object> payload = Map.of(
//                "model", "gpt-4o-mini",
//                "messages", List.of(Map.of("role", "user", "content", request.message())),
//                "temperature", 0.7 
//            );
//
//            String json = mapper.writeValueAsString(payload);
//
//            HttpRequest httpRequest = HttpRequest.newBuilder()
//                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
//                    .header("Content-Type", "application/json")
//                    .header("Authorization", "Bearer " + openaiKey)
//                    .POST(HttpRequest.BodyPublishers.ofString(json))
//                    .build();
//
//            HttpClient httpClient = HttpClient.newHttpClient();
//            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() != 200) {
//                return "Lỗi từ OpenAI API: " + response.body();
//            }
//
//            var jsonNode = mapper.readTree(response.body());
//            String content = jsonNode
//                    .path("choices")
//                    .get(0)
//                    .path("message")
//                    .path("content")
//                    .asText();
//
//            return content;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Lỗi khi gọi AI: " + e.getMessage();
//        }
//    }
//}
