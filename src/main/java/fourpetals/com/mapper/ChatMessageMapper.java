package fourpetals.com.mapper;

import fourpetals.com.dto.request.ChatMessageRequest;
import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.ChatMessage;

public class ChatMessageMapper {
	public static ChatMessage toEntity(ChatMessageRequest req) {
		ChatMessage msg = new ChatMessage();
		msg.setContent(req.getContent());
		msg.setHanhDong(req.getHanhDong());
		msg.setNoiBo(req.isNoiBo());

		// Chỉ gán tên khách nếu chat khách
		if (!req.isNoiBo()) {
			msg.setNguoiGuiKhach(req.getNguoiGuiKhach());
			msg.setNguoiNhanKhach(req.getNguoiNhanKhach());
		}

		return msg;
	}

	// Entity -> Response DTO
	public static ChatMessageResponse toResponse(ChatMessage msg) {
		ChatMessageResponse res = new ChatMessageResponse();
		res.setId(msg.getId());
		res.setContent(msg.getContent());
		res.setHanhDong(msg.getHanhDong());
		res.setNoiBo(msg.isNoiBo());
		res.setTimestamp(msg.getTimestamp());

		if (msg.isNoiBo()) {
			if (msg.getNguoiGui() != null)
				res.setNguoiGuiId(msg.getNguoiGui().getUserId());
			if (msg.getNguoiNhan() != null)
				res.setNguoiNhanId(msg.getNguoiNhan().getUserId());
			if (msg.getNhanVienPhuTrach() != null)
				res.setNhanVienPhuTrachId(msg.getNhanVienPhuTrach().getUserId());
		} else {
			res.setNguoiGuiKhach(msg.getNguoiGuiKhach());
			res.setNguoiNhanKhach(msg.getNguoiNhanKhach());
		}

		return res;
	}
}
