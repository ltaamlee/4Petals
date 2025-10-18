console.log('JS loaded'); 
document.addEventListener('DOMContentLoaded', () => {
    const chatButton = document.getElementById('chatButton');
    const chatContainer = document.getElementById('chatContainer');
    const chatMessages = document.getElementById('chatMessages');
    const chatInput = document.getElementById('chatInput');
    const sendButton = document.getElementById('sendButton');

    if (!chatButton || !chatContainer) {
        console.error('Chat elements not found!');
        return;
    }

    chatButton.addEventListener('click', () => {
        console.log("Chat button clicked");
        chatButton.classList.toggle('active');
        chatContainer.classList.toggle('active');
        if (chatContainer.classList.contains('active')) chatInput.focus();
    });

    function addMessage(text, isUser = false) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${isUser ? 'user' : 'bot'}`;
        messageDiv.innerHTML = `
            ${!isUser ? '<div class="message-avatar">🌸</div>' : ''}
            <div><div class="message-content">${text}</div></div>
        `;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function getBotResponse(message) {
        const msg = message.toLowerCase();
        if (msg.includes('mua') || msg.includes('hoa')) {
            return 'Chúng tôi có nhiều loại hoa tươi, bạn thích loại nào?';
        } else if (msg.includes('đặt')) {
            return 'Bạn có thể đặt hoa online và chọn ngày giao hàng.';
        } else if (msg.includes('sự kiện')) {
            return 'Chúng tôi cung cấp dịch vụ trang trí hoa cho sự kiện.';
        } else {
            return 'Cảm ơn bạn! Tư vấn viên sẽ liên hệ sớm.';
        }
    }

    function sendMessage() {
        const text = chatInput.value.trim();
        if (!text) return;
        addMessage(text, true);
        chatInput.value = '';
        setTimeout(() => addMessage(getBotResponse(text), false), 800 + Math.random() * 500);
    }

    sendButton.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', e => { if (e.key === 'Enter') sendMessage(); });

    document.querySelectorAll('.quick-reply-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            chatInput.value = btn.getAttribute('data-message');
            sendMessage();
        });
    });
});
