package fourpetals.com.mapper;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatMessage;
import fourpetals.com.entity.ChatRoom;
import fourpetals.com.entity.User;

public class ChatMessageMapper {
	 /**
     * Chuyển ChatMessageRequest thành ChatMessage entity
     * @param request
     * @param nguoiGui user entity gửi (null nếu guest)
     * @param nguoiNhan user entity nhận (null nếu guest)
     * @param nhanVienPhuTrach user phụ trách ca trực (null nếu chat nội bộ)
     * @param chatRoom ChatRoom tương ứng
     * @return ChatMessage
     */
	public static ChatMessage toEntity(
	        ChatMessageRequest request,
	        User nguoiGui,
	        User nguoiNhan,
	        User nhanVienPhuTrach,
	        ChatRoom chatRoom
	) {
	    ChatMessage msg = new ChatMessage();

	    // --- Người gửi ---
	    msg.setNguoiGui(nguoiGui); // null nếu guest
	    if (nguoiGui == null) {
	        // Nếu khách không có tên thì tạo tên mặc định theo session
	        if (request.getTenNguoiGuiKhach() == null || request.getTenNguoiGuiKhach().isBlank()) {
	            msg.setTenNguoiGuiKhach("Khách-" + request.getSessionId());
	        } else {
	            msg.setTenNguoiGuiKhach(request.getTenNguoiGuiKhach());
	        }
	    }

	    // --- Người nhận ---
	    msg.setNguoiNhan(nguoiNhan); // null nếu guest
	    msg.setTenNguoiNhanKhach(request.getTenNguoiNhanKhach());

	    // --- Nội dung & metadata ---
	    msg.setNoiDung(request.getNoiDung());
	    msg.setNoiBo(Boolean.TRUE.equals(request.getNoiBo()));
	    msg.setHanhDong(request.getHanhDong());
	    msg.setChatRoom(chatRoom);
	    msg.setNhanVienPhuTrach(nhanVienPhuTrach); // null nếu chat nội bộ

	    return msg;
	}

    public static ChatMessageResponse toResponse(ChatMessage msg) {
        Integer senderId = msg.getNguoiGui() != null ? msg.getNguoiGui().getUserId() : null;
        Integer receiverId = msg.getNguoiNhan() != null ? msg.getNguoiNhan().getUserId() : null;
        Integer nhanVienPhuTrachId = msg.getNhanVienPhuTrach() != null ? msg.getNhanVienPhuTrach().getUserId() : null;
        String roomId = msg.getChatRoom() != null ? msg.getChatRoom().getRoomId() : null;

        return new ChatMessageResponse(
                msg.getId(),
                senderId,
                msg.getTenNguoiGuiKhach(),
                receiverId,
                msg.getTenNguoiNhanKhach(),
                msg.getNoiDung(),
                msg.isNoiBo(),
                msg.getHanhDong(),
                roomId,
                msg.getThoiGianGui(),
                nhanVienPhuTrachId
        );
    }
}
