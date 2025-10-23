console.log('Chat JS loaded (Combined Version - Patched)');

document.addEventListener('DOMContentLoaded', () => {

    // --- 1. Lấy TẤT CẢ các phần tử DOM cần thiết ---
    const chatButton = document.getElementById('chatButton');
    const chatContainer = document.getElementById('chatContainer');
    const chatMessages = document.getElementById('chatMessages');
    const chatInput = document.getElementById('chatInput');
    const sendButton = document.getElementById('sendButton');

    // Lấy các thẻ meta một cách an toàn
    const metaSession = document.querySelector('meta[name="session-id"]');
    const metaRoom = document.querySelector('meta[name="room-id"]');
    
    // ✅ SỬA LỖI 1: Thêm thẻ meta cho user-id
    const metaUserId = document.querySelector('meta[name="user-id"]');

    // --- 2. Kiểm tra lỗi nghiêm trọng (Thiếu DOM) ---
    if (!chatButton || !chatContainer || !chatMessages || !chatInput || !sendButton) {
        console.error('LỖI NGHIÊM TRỌNG: Thiếu một trong các phần tử chat cơ bản. Script dừng lại.');
        return; // Dừng script ngay lập tức
    }

    // --- 3. Lấy thông tin (An toàn) ---
    const sessionId = (metaSession && metaSession.content) ? metaSession.content : null;
    const roomId = (metaRoom && metaRoom.content) ? metaRoom.content : null;
    
    // ✅ SỬA LỖI 1: Đọc UserID từ meta.
    // Nếu thẻ meta tồn tại VÀ có nội dung (không phải chuỗi rỗng ''),
    // thì chuyển nó thành số. Ngược lại, nó là null (Guest).
    const currentUserId = (metaUserId && metaUserId.content) ? parseInt(metaUserId.content) : null;

    console.log("📡 sessionId:", sessionId, "roomId:", roomId, "currentUserId:", currentUserId);

    const isInternalChat = false;
    const receiverId = 1; // Nhân viên phụ trách (Giả định ID = 1)
    window.stompClient = null;

    // --- 4. Định nghĩa tất cả các hàm ---

    /**
     * Thêm tin nhắn vào giao diện
     */
    function addMessage(text, isUser = false) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message flex ${isUser ? 'user justify-end' : 'bot'}`;
        messageDiv.innerHTML = `
            ${!isUser ? '<div class="message-avatar">🌸</div>' : ''}
            <div><div class="message-content">${text}</div></div>
        `;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    /**
     * Lấy phản hồi tự động (fallback)
     */
    function getBotResponse(message) {
        const msg = message.toLowerCase();
        if (msg.includes('mua') || msg.includes('hoa')) return 'Chúng tôi có nhiều loại hoa tươi, bạn thích loại nào?';
        if (msg.includes('đặt')) return 'Bạn có thể đặt hoa online và chọn ngày giao hàng.';
        if (msg.includes('sự kiện')) return 'Chúng tôi cung cấp dịch vụ trang trí hoa cho sự kiện.';
        return 'Cảm ơn bạn! Tư vấn viên sẽ liên hệ sớm.';
    }

    /**
     * Hàm chính để gửi tin nhắn
     */
    function sendMessage() {
        const text = chatInput.value.trim();
        if (!text) return;

        addMessage(text, true);
        chatInput.value = '';

        if (window.stompClient && window.stompClient.connected) {
            
            // ✅ SỬA LỖI 2:
            // Chỉ cần kiểm tra roomId. 
            // Guest sẽ có (currentUserId=null, CÓ sessionId).
            // Customer sẽ có (CÓ currentUserId, sessionId=null).
            // Logic check mới: "Phòng có tồn tại KHÔNG?" VÀ 
            // "Nếu là Guest, thì sessionId cũng phải tồn tại?"
            if (!roomId || (currentUserId === null && !sessionId)) {
                console.error('Không thể gửi tin nhắn WS: thiếu roomId, hoặc là Guest nhưng thiếu sessionId.');
                addMessage('Lỗi kết nối. Vui lòng tải lại trang.', false);
                return;
            }

            const payload = {
                noiDung: text,
                nguoiGuiId: currentUserId, // Sẽ là null nếu là Guest
                nguoiNhanId: receiverId,
                sessionId: sessionId, // Sẽ là null nếu là Customer
                noiBo: isInternalChat,
                roomId: roomId
            };
            window.stompClient.send("/app/send", {}, JSON.stringify(payload));
        
        } else {
            // FALLBACK BOT
            console.log('Không có kết nối WebSocket, sử dụng bot fallback.');
            setTimeout(() => addMessage(getBotResponse(text), false), 800 + Math.random() * 500);
        }
        
        chatInput.focus();
    }

    // --- 5. Khởi tạo WebSocket ---

    function initWebSocket() {
        if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') {
            console.warn('SockJS/Stomp chưa được tải. Chat sẽ ở chế độ offline (bot fallback).');
            return;
        }

        // ✅ SỬA LỖI 2 (Tương tự như sendMessage):
        // Kiểm tra các điều kiện cần thiết để KẾT NỐI VÀ SUBSCRIBE
        if (!roomId) {
            console.warn('Không thể khởi tạo WebSocket: Thiếu roomId.');
            return;
        }
        
        // Nếu là guest nhưng lại thiếu sessionId thì cũng không subscribe được
        if (roomId.startsWith('guest-') && !sessionId) {
            console.warn('Không thể khởi tạo WebSocket: Guest nhưng thiếu sessionId.');
            return;
        }

        // Nếu là customer nhưng (vì lỗi nào đó) lại thiếu userId thì cũng không subscribe được
        if (roomId.startsWith('customer-') && currentUserId === null) {
            console.warn('Không thể khởi tạo WebSocket: Customer nhưng thiếu currentUserId.');
            return;
        }


        try {
            const socket = new SockJS('/ws-chat');
            const stompClient = Stomp.over(socket);
            window.stompClient = stompClient;

            stompClient.connect({}, frame => {
                console.log('Connected:', frame);
                if (roomId.startsWith('guest-')) {
                    subscribeGuestMessages();
                } else if (roomId.startsWith('customer-')) {
                    subscribeCustomerMessages();
                }
            }, error => {
                console.error('STOMP connection error:', error);
                window.stompClient = null;
            });

        } catch (e) {
            console.error('Lỗi khởi tạo WebSocket:', e);
            window.stompClient = null;
        }
    }

    function subscribeGuestMessages() {
        // Phải chắc chắn sessionId có tồn tại (đã check ở initWebSocket)
        const topic = "/topic/chat-guest-" + sessionId;
        window.stompClient.subscribe(topic, message => {
            const msg = JSON.parse(message.body);
            if (msg.sessionId === sessionId) {
                addMessage(msg.noiDung, false);
            }
        });
        console.log('Subscribed to guest messages:', topic);
    }

    function subscribeCustomerMessages() {
        // Phải chắc chắn currentUserId có tồn tại (đã check ở initWebSocket)
        const topic = "/topic/chat-user-" + currentUserId;
        window.stompClient.subscribe(topic, message => {
            const msg = JSON.parse(message.body);
            // Chỉ hiển thị tin nhắn không phải của chính mình
            if (msg.nguoiGuiId !== currentUserId) {
                addMessage(msg.noiDung, false);
            }
        });
        console.log('Subscribed to customer messages:', topic);
    }

    // --- 6. Gán sự kiện (Không thay đổi) ---

    chatButton.addEventListener('click', () => {
        chatButton.classList.toggle('active');
        chatContainer.classList.toggle('active');
        if (chatContainer.classList.contains('active')) chatInput.focus();
    });

    sendButton.addEventListener('click', sendMessage);
    
    chatInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') {
            e.preventDefault(); 
            sendMessage();
        }
    });

    document.querySelectorAll('.quick-reply-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const messageText = btn.getAttribute('data-message');
            chatInput.value = messageText;
            sendMessage();
        });
    });

    // --- 7. Khởi chạy ---
    initWebSocket();

});