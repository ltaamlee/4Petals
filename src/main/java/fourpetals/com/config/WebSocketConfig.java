package fourpetals.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Tên các topic mà client sẽ subscribe
        config.enableSimpleBroker("/topic");  // broker in-memory cho demo
        // Prefix gửi message từ client lên controller
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint mà client sẽ connect
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")  // Cho phép tất cả domain, sửa thành domain của bạn khi deploy
                .withSockJS();  // Hỗ trợ fallback nếu WebSocket không khả dụng
    }
}
