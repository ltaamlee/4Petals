console.log('Employee Chat JS loaded');

document.addEventListener('DOMContentLoaded', () => {
    const chatRoomsList = document.getElementById('chatRooms');
    const chatMessages = document.getElementById('chatMessages');
    const chatInput = document.getElementById('chatInput');
    const sendButton = document.getElementById('sendButton');
    const chatHeader = document.getElementById('chatHeader');

    const metaUserId = document.querySelector('meta[name="user-id"]');
    const currentUserId = metaUserId?.content ? parseInt(metaUserId.content) : null;

    let activeRoomId = null;
    let currentReceiverId = null;
    window.stompClient = null;
    const subscribedRooms = new Set(); // Lưu các room đã subscribe

    // --- Hiển thị tin nhắn ---
    function addMessage(text, senderName = 'Guest', isUser = false) {
        const msgDiv = document.createElement('div');
        msgDiv.className = `message ${isUser ? 'user justify-end' : 'bot'}`;
        msgDiv.innerHTML = `
            ${!isUser ? `<div class="message-avatar">${senderName[0].toUpperCase()}</div>` : ''}
            <div><div class="message-content">${text}</div></div>
        `;
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // --- Load lịch sử tin nhắn ---
    function loadMessages(roomId) {
        fetch(`/api/chat/room/${roomId}`)
            .then(res => res.json())
            .then(data => {
                chatMessages.innerHTML = '';
                data.forEach(msg => {
                    const isUser = msg.nguoiGuiId === currentUserId;
                    const senderName = isUser ? 'Bạn' : msg.nguoiGuiUsername || 'Guest';
                    addMessage(msg.noiDung, senderName, isUser);
                });
            })
            .catch(err => {
                console.error('Lỗi load messages:', err);
                chatMessages.innerHTML = '<div class="text-center text-gray">Không thể tải tin nhắn</div>';
            });
    }

    // --- Thêm phòng mới ---
    function addRoom(roomId, roomName) {
        if (!document.querySelector(`.chat-room[data-room-id="${roomId}"]`)) {
            const li = document.createElement('li');
            li.className = 'chat-room';
            li.dataset.roomId = roomId;
            li.textContent = roomName;
            chatRoomsList.appendChild(li);

            li.addEventListener('click', () => {
                selectRoom(roomId, roomName);
            });
        }
    }

    // --- Chọn phòng chat ---
    function selectRoom(roomId, roomName) {
        activeRoomId = roomId;
        chatHeader.textContent = roomName;
        chatMessages.innerHTML = '<div class="text-center text-gray">Đang tải tin nhắn...</div>';

        currentReceiverId = roomId.startsWith('customer-') ? parseInt(roomId.split('-')[1]) : null;

        // Subscribe topic phòng nếu chưa subscribe
        if (!subscribedRooms.has(roomId)) {
            subscribeRoom(roomId);
        }

        loadMessages(roomId);
    }

    // --- Subscribe topic cho phòng ---
    function subscribeRoom(roomId) {
        if (!window.stompClient || !roomId) return;

        let topic = '';
        if (roomId.startsWith('guest-')) {
            topic = `/topic/chat-${roomId}`;
        } else if (roomId.startsWith('customer-')) {
            topic = `/topic/chat-user-${currentReceiverId}`;
        }

        window.stompClient.subscribe(topic, message => {
            const msg = JSON.parse(message.body);

            // Nếu là guest mới, tự tạo room
            if (roomId.startsWith('guest-') && !document.querySelector(`.chat-room[data-room-id="${roomId}"]`)) {
                addRoom(roomId, `Guest 🌸 ${roomId.split('-')[1]}`);
            }

            const isUser = msg.nguoiGuiId === currentUserId;
            const senderName = isUser ? 'Bạn' : msg.nguoiGuiUsername || 'Guest';

            // Hiển thị nếu là room đang chọn
            if (activeRoomId === roomId) {
                addMessage(msg.noiDung, senderName, isUser);
            }
        });

        subscribedRooms.add(roomId);
        console.log('Subscribed to room:', topic);
    }

    // --- Gửi tin nhắn ---
    function sendMessage() {
        if (!activeRoomId || !chatInput.value.trim()) return;

        const payload = {
            noiDung: chatInput.value.trim(),
            nguoiGuiId: currentUserId,
            nguoiNhanId: currentReceiverId,
            sessionId: activeRoomId.startsWith('guest-') ? activeRoomId.split('-')[1] : null,
            roomId: activeRoomId,
            noiBo: true
        };

        if (window.stompClient && window.stompClient.connected) {
            window.stompClient.send('/app/send', {}, JSON.stringify(payload));
            addMessage(chatInput.value.trim(), 'Bạn', true);
            chatInput.value = '';
        } else {
            console.warn('Chưa kết nối WebSocket');
        }
    }

    sendButton.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            sendMessage();
        }
    });

    // --- Khởi tạo WebSocket ---
    function initWebSocket() {
        if (!currentUserId) {
            console.warn('Thiếu currentUserId cho nhân viên.');
            return;
        }

        const socket = new SockJS('/ws-chat');
        const stompClient = Stomp.over(socket);
        window.stompClient = stompClient;

        stompClient.connect({}, frame => {
            console.log('STOMP Connected:', frame);

            // Khi guest mới nhắn, tạo room tự động
            stompClient.subscribe(`/topic/chat-new-guest`, message => {
                const msg = JSON.parse(message.body);
                const roomId = `guest-${msg.sessionId}`;
                const roomName = `Guest 🌸 ${msg.sessionId}`;
                addRoom(roomId, roomName);
                subscribeRoom(roomId); // Subscribe ngay khi room mới
            });

            // Subscribe tất cả tin nhắn employee riêng
            stompClient.subscribe(`/topic/chat-user-${currentUserId}`, message => {
                const msg = JSON.parse(message.body);
                const roomId = `customer-${msg.nguoiGuiId}`;
                const roomName = `Customer 💐 ${msg.nguoiGuiUsername || msg.nguoiGuiId}`;
                addRoom(roomId, roomName);
                subscribeRoom(roomId);

                // Nếu đang chọn room này, hiển thị tin nhắn
                if (activeRoomId === roomId) {
                    addMessage(msg.noiDung, msg.nguoiGuiUsername || 'Customer', false);
                }
            });

        }, error => {
            console.error('STOMP connection error:', error);
        });
    }

    initWebSocket();
});
