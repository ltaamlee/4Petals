package fourpetals.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Cấu hình message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic: gửi cho tất cả client subscribe
        // /queue: gửi riêng cho 1 client
        config.enableSimpleBroker("/topic", "/queue"); 
        // /app: prefix cho các message từ client gửi lên server
        config.setApplicationDestinationPrefixes("/app");
    }

    // Cấu hình endpoint WebSocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws-chat: endpoint cho client connect WebSocket
        // SockJS fallback nếu trình duyệt không hỗ trợ WebSocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
