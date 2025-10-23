package fourpetals.com.dto.request;

import fourpetals.com.enums.ChatAction;

public class ChatMessageRequest {
	private Integer nguoiGuiId; // User.id nếu có, null nếu guest
	private Integer nguoiNhanId; // User.id nếu có, null nếu khách
	private String tenNguoiGuiKhach; // khách gửi
	private String tenNguoiNhanKhach; // khách nhận
	private String noiDung; // nội dung tin nhắn
	private Boolean noiBo; // true = chat nội bộ, false = chat khách
	private String sessionId; // sessionId của guest
	private String roomId; // teamId / roomId chat nội bộ
	private ChatAction hanhDong;

	public Integer getNguoiGuiId() {
		return nguoiGuiId;
	}

	public void setNguoiGuiId(Integer nguoiGuiId) {
		this.nguoiGuiId = nguoiGuiId;
	}

	public Integer getNguoiNhanId() {
		return nguoiNhanId;
	}

	public void setNguoiNhanId(Integer nguoiNhanId) {
		this.nguoiNhanId = nguoiNhanId;
	}

	public String getTenNguoiGuiKhach() {
		return tenNguoiGuiKhach;
	}

	public void setTenNguoiGuiKhach(String tenNguoiGuiKhach) {
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
	}

	public String getTenNguoiNhanKhach() {
		return tenNguoiNhanKhach;
	}

	public void setTenNguoiNhanKhach(String tenNguoiNhanKhach) {
		this.tenNguoiNhanKhach = tenNguoiNhanKhach;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public Boolean getNoiBo() {
		return noiBo;
	}

	public void setNoiBo(Boolean noiBo) {
		this.noiBo = noiBo;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public ChatAction getHanhDong() {
		return hanhDong;
	}

	public void setHanhDong(ChatAction hanhDong) {
		this.hanhDong = hanhDong;
	}

	public ChatMessageRequest(Integer nguoiGuiId, Integer nguoiNhanId, String tenNguoiGuiKhach,
			String tenNguoiNhanKhach, String noiDung, Boolean noiBo, String sessionId, String roomId,
			ChatAction hanhDong) {
		super();
		this.nguoiGuiId = nguoiGuiId;
		this.nguoiNhanId = nguoiNhanId;
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
		this.tenNguoiNhanKhach = tenNguoiNhanKhach;
		this.noiDung = noiDung;
		this.noiBo = noiBo;
		this.sessionId = sessionId;
		this.roomId = roomId;
		this.hanhDong = hanhDong;
	}

	public ChatMessageRequest() {
		super();
	}

}
