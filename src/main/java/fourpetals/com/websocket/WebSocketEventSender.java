package fourpetals.com.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class WebSocketEventSender {
	@Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Gửi event tới tất cả client subscribe topic
     * @param topic ví dụ: "/topic/order-cancel-status"
     * @param payload dữ liệu gửi đi
     */
    public void sendToTopic(String topic, Object payload) {
        messagingTemplate.convertAndSend(topic, payload);
    }

    /**
     * Gửi event riêng cho 1 client (queue)
     * @param userQueue ví dụ: "/queue/chat/123"
     * @param payload dữ liệu gửi
     */
    public void sendToUser(String userQueue, Object payload) {
        messagingTemplate.convertAndSend(userQueue, payload);
    }
}
