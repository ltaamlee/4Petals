//package fourpetals.com.service.impl;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import fourpetals.com.dto.chat.ChatAIRequest;
//import fourpetals.com.entity.Category;
//import fourpetals.com.entity.Product;
//import fourpetals.com.entity.Promotion;
//import fourpetals.com.repository.CategoryRepository;
//import fourpetals.com.repository.ProductRepository;
//import fourpetals.com.repository.PromotionRepository;
//import fourpetals.com.service.ChatAIService;
//import jakarta.annotation.PostConstruct;
//
//@Service
//public class ChatAIServiceImpl implements ChatAIService {
//
//    private final ProductRepository productRepo;
//    private final CategoryRepository categoryRepo;
//    private final PromotionRepository promotionRepo;
//
//    @Value("${OPENAI_KEY}")
//    private String apiKey;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public ChatAIServiceImpl(ProductRepository productRepo,
//                             CategoryRepository categoryRepo,
//                             PromotionRepository promotionRepo) {
//        this.productRepo = productRepo;
//        this.categoryRepo = categoryRepo;
//        this.promotionRepo = promotionRepo;
//    }
//
//    @PostConstruct
//    public void init() {
//        System.out.println("API Key loaded (Google Cloud Gemini): " + apiKey);
//    }
//
//    @Override
//    public String askAI(ChatAIRequest request) {
//        try {
//            // --- Lấy dữ liệu từ DB ---
//            List<Product> products = productRepo.findAll().stream().distinct().collect(Collectors.toList());
//            List<Category> categories = categoryRepo.findAll();
//            List<Promotion> promotions = promotionRepo.findAllActive(LocalDateTime.now());
//
//            // --- Xây dựng prompt ---
//            StringBuilder prompt = new StringBuilder();
//            prompt.append("Bạn là tư vấn viên bán hoa. Dưới đây là dữ liệu tham khảo:\n\n");
//
//            prompt.append("Sản phẩm:\n");
//            for (Product p : products) {
//                prompt.append("- ").append(p.getTenSP())
//                      .append(": ").append(p.getMoTa() != null ? p.getMoTa() : "")
//                      .append("\n");
//            }
//
//            prompt.append("\nDanh mục:\n");
//            for (Category c : categories) {
//                prompt.append("- ").append(c.getTenDM())
//                      .append(": ").append(c.getMoTa() != null ? c.getMoTa() : "")
//                      .append("\n");
//            }
//
//            prompt.append("\nKhuyến mãi đang hoạt động:\n");
//            for (Promotion km : promotions) {
//                prompt.append("- ").append(km.getTenkm())
//                      .append(" (").append(km.getLoaiKm() != null ? km.getLoaiKm() : "")
//                      .append(")\n");
//            }
//
//            prompt.append("\nCâu hỏi của khách: ").append(request.message()).append("\n");
//            prompt.append("Hãy trả lời chi tiết, thân thiện và tư vấn theo dữ liệu trên.");
//
//            // --- Gọi Gemini API ---
//            return callGeminiApi(prompt.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return buildErrorResponse("Lỗi khi gọi AI: " + e.getMessage());
//        }
//    }
//
//    private String callGeminiApi(String prompt) {
//        try {
//            Map<String, Object> contentPart = Map.of("text", prompt);
//            Map<String, Object> userContent = Map.of("role", "user", "parts", List.of(contentPart));
//            Map<String, Object> payload = Map.of(
//                    "contents", List.of(userContent),
//                    "generationConfig", Map.of(
//                            "temperature", 0.7,
//                            "maxOutputTokens", 1024
//                    )
//            );
//
//            String json = mapper.writeValueAsString(payload);
//            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
//
//            HttpRequest httpRequest = HttpRequest.newBuilder()
//                    .uri(URI.create(endpoint))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(json))
//                    .build();
//
//            HttpClient httpClient = HttpClient.newHttpClient();
//            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() != 200) {
//                System.err.println("Gemini API trả lỗi: " + response.body());
//                return buildErrorResponse("Xin lỗi, AI hiện không phản hồi được.");
//            }
//
//            // --- Xử lý kết quả ---
//            JsonNode root = mapper.readTree(response.body());
//            JsonNode candidates = root.path("candidates");
//            if (candidates.isArray() && candidates.size() > 0) {
//                JsonNode parts = candidates.get(0).path("content").path("parts");
//                if (parts.isArray() && parts.size() > 0) {
//                    // Lấy text thật sự, không embed JSON string
//                    String contentText = StreamSupport.stream(parts.spliterator(), false)
//                            .map(p -> p.path("text").asText())
//                            .collect(Collectors.joining("\n"));
//
//                    // Trả về JSON đúng cấu trúc cho frontend
//                    return mapper.writeValueAsString(Map.of(
//                        "candidates", List.of(Map.of("text", contentText))
//                    ));
//                }
//            }
//
//            return buildErrorResponse("AI không trả về nội dung hợp lệ.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return buildErrorResponse("Lỗi khi gọi AI: " + e.getMessage());
//        }
//    }
//
//    private String buildErrorResponse(String message) {
//        try {
//            return mapper.writeValueAsString(Map.of(
//                "candidates", List.of(Map.of("text", message))
//            ));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "{\"candidates\":[{\"text\":\"Lỗi không xác định.\"}]}";
//        }
//    }
//} 
