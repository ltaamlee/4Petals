package fourpetals.com.dto.response;

import java.time.LocalDateTime;

import fourpetals.com.enums.ChatAction;

public class ChatMessageResponse {
	private Integer id;
	private Integer nguoiGuiId;
	private String nguoiGuiKhach;
	private Integer nguoiNhanId;
	private String nguoiNhanKhach;
	private String content;
	private ChatAction hanhDong;
	private boolean isNoiBo;
	private Integer nhanVienPhuTrachId;
	private LocalDateTime timestamp;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
