package fourpetals.com.dto.request;

import fourpetals.com.enums.ChatAction;

public class ChatMessageRequest {
	private Integer nguoiGuiId; // userId nếu là nhân viên
	private String nguoiGuiKhach; // tên khách nếu là khách
	private Integer nguoiNhanId; // userId nếu là nhân viên
	private String nguoiNhanKhach; // tên khách nếu là khách
	private String content;
	private ChatAction hanhDong;
	private boolean isNoiBo;
	private Integer nhanVienPhuTrachId;
	public Integer getNguoiGuiId() {
		return nguoiGuiId;
	}
	public void setNguoiGuiId(Integer nguoiGuiId) {
		this.nguoiGuiId = nguoiGuiId;
	}
	public String getNguoiGuiKhach() {
		return nguoiGuiKhach;
	}
	public void setNguoiGuiKhach(String nguoiGuiKhach) {
		this.nguoiGuiKhach = nguoiGuiKhach;
	}
	public Integer getNguoiNhanId() {
		return nguoiNhanId;
	}
	public void setNguoiNhanId(Integer nguoiNhanId) {
		this.nguoiNhanId = nguoiNhanId;
	}
	public String getNguoiNhanKhach() {
		return nguoiNhanKhach;
	}
	public void setNguoiNhanKhach(String nguoiNhanKhach) {
		this.nguoiNhanKhach = nguoiNhanKhach;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ChatAction getHanhDong() {
		return hanhDong;
	}
	public void setHanhDong(ChatAction hanhDong) {
		this.hanhDong = hanhDong;
	}
	public boolean isNoiBo() {
		return isNoiBo;
	}
	public void setNoiBo(boolean isNoiBo) {
		this.isNoiBo = isNoiBo;
	}
	public Integer getNhanVienPhuTrachId() {
		return nhanVienPhuTrachId;
	}
	public void setNhanVienPhuTrachId(Integer nhanVienPhuTrachId) {
		this.nhanVienPhuTrachId = nhanVienPhuTrachId;
	}
	
	
}
