const chatMessages = document.getElementById("chatMessages");
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");

// Demo dữ liệu ban đầu
let messages = [
    { sender: "me", content: "Chào bạn!", timestamp: "10:00" },
    { sender: "other", content: "Chào bạn, tôi cần hỗ trợ.", timestamp: "10:01" },
];

// Hàm render tin nhắn
function renderMessages() {
    chatMessages.innerHTML = "";
    messages.forEach(msg => {
        const div = document.createElement("div");
        div.classList.add("message");
        div.classList.add(msg.sender === "me" ? "sent" : "received");
        div.innerHTML = `${msg.content} <span class="timestamp">${msg.timestamp}</span>`;
        chatMessages.appendChild(div);
    });

    // scroll tự động xuống cuối
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Gửi tin nhắn
sendBtn.addEventListener("click", () => {
    const text = messageInput.value.trim();
    if (text === "") return;

    const now = new Date();
    const timestamp = now.getHours().toString().padStart(2, "0") + ":" + now.getMinutes().toString().padStart(2, "0");

    messages.push({ sender: "me", content: text, timestamp });
    messageInput.value = "";
    renderMessages();
});

// Enter để gửi
messageInput.addEventListener("keydown", (e) => {
    if (e.key === "Enter") sendBtn.click();
});

// Render ban đầu
renderMessages();
