console.log('Chat JS loaded (Quick Chat Mode)');

document.addEventListener('DOMContentLoaded', () => {
    const chatButton = document.getElementById('chatButton');
    const chatContainer = document.getElementById('chatContainer');
    const chatMessages = document.getElementById('chatMessages');
    const chatInput = document.getElementById('chatInput');
    const sendButton = document.getElementById('sendButton');

    if (!chatButton || !chatContainer || !chatMessages || !chatInput || !sendButton) {
        console.error('Thiếu phần tử chat cơ bản. Dừng script.');
        return;
    }

    // --- Hiển thị tin nhắn ---
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

    // --- Trả lời nhanh ---
    function quickReply(userMessage) {
        const replies = {
            "xin chào": "Chào bạn! Shop rất vui được đón bạn 🌸",
            "mua hoa": "Bạn muốn mua loại hoa nào ạ?",
            "hoa sinh nhật": "Shop có nhiều bó hoa sinh nhật đẹp, bạn muốn xem không?",
            "default": "Shop đang xử lý thông tin, bạn vui lòng để lại tin nhắn nhé!"
        };

        const key = userMessage.toLowerCase();
        return replies[key] || replies["default"];
    }

    // --- Gửi tin nhắn ---
    function sendMessage(text) {
        if (!text) return;

        addMessage(text, true); // Tin nhắn người dùng

        // Trả lời nhanh
        const reply = quickReply(text);
        addMessage(reply, false);

        chatInput.value = '';
        chatInput.focus();
    }

    // --- Sự kiện gõ enter ---
    chatInput.addEventListener('keydown', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            sendMessage(chatInput.value.trim());
        }
    });

    sendButton.addEventListener('click', () => {
        sendMessage(chatInput.value.trim());
    });

    // --- Quick reply button ---
    document.querySelectorAll('.quick-reply-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const messageText = btn.getAttribute('data-message');
            sendMessage(messageText); // Gửi ngay lập tức
        });
    });

    // --- Hiển thị/ẩn chat ---
    chatButton.addEventListener('click', () => {
        chatButton.classList.toggle('active');
        chatContainer.classList.toggle('active');
        if (chatContainer.classList.contains('active')) chatInput.focus();
    });
});
