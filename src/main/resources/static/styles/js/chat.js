console.log('Chat JS loaded (Fallback AI Mode)');

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

	// --- Hàm hiển thị tin nhắn ---
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

	// --- Hàm gửi tin nhắn lên API Chat AI ---
	async function getBotResponse(message) {
		try {
			const res = await fetch('/api/chat-ai', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ message: message })
			});
			const text = await res.text();
			return text
		} catch (err) {
			console.error('Lỗi gọi Chat AI:', err);
			return 'Xin lỗi, AI hiện không phản hồi được.';
		}
	}

	// --- Hàm gửi tin nhắn ---
	async function sendMessage() {
		const text = chatInput.value.trim();
		if (!text) return;

		addMessage(text, true);
		chatInput.value = '';
		addMessage('...Đang trả lời AI', false);

		const botReply = await getBotResponse(text);
		// Xóa tin nhắn "Đang trả lời AI"
		const lastMessage = chatMessages.lastChild;
		if (lastMessage && lastMessage.querySelector('.message-content').textContent.includes('...Đang trả lời AI')) {
			chatMessages.removeChild(lastMessage);
		}

		addMessage(botReply, false);
		chatInput.focus();
	}

	// --- Gán sự kiện ---
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

});
