package fourpetals.com.dto.response;

import java.time.LocalDateTime;

import fourpetals.com.enums.ChatAction;

public class ChatMessageResponse {
	private Integer id;
    private Integer nguoiGuiId;             // nguoiGui.id
    private String tenNguoiGuiKhach;      // khách gửi
    private Integer nguoiNhanId;           // nguoiNhan.id
    private String tenNguoiNhanKhach;     // khách nhận
    private String noiDung;
    private Boolean noiBo;
    private ChatAction hanhDong;
    private String roomId;                // ChatRoom.roomId
    private LocalDateTime thoiGianGui;
    private Integer nhanVienPhuTrachId;
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
	public String getTenNguoiGuiKhach() {
		return tenNguoiGuiKhach;
	}
	public void setTenNguoiGuiKhach(String tenNguoiGuiKhach) {
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
	}
	public Integer getNguoiNhanId() {
		return nguoiNhanId;
	}
	public void setNguoiNhanId(Integer nguoiNhanId) {
		this.nguoiNhanId = nguoiNhanId;
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
	public ChatAction getHanhDong() {
		return hanhDong;
	}
	public void setHanhDong(ChatAction hanhDong) {
		this.hanhDong = hanhDong;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public LocalDateTime getThoiGianGui() {
		return thoiGianGui;
	}
	public void setThoiGianGui(LocalDateTime thoiGianGui) {
		this.thoiGianGui = thoiGianGui;
	}
	public Integer getNhanVienPhuTrachId() {
		return nhanVienPhuTrachId;
	}
	public void setNhanVienPhuTrachId(Integer nhanVienPhuTrachId) {
		this.nhanVienPhuTrachId = nhanVienPhuTrachId;
	}
	public ChatMessageResponse(Integer id, Integer nguoiGuiId, String tenNguoiGuiKhach, Integer nguoiNhanId,
			String tenNguoiNhanKhach, String noiDung, Boolean noiBo, ChatAction hanhDong, String roomId,
			LocalDateTime thoiGianGui, Integer nhanVienPhuTrachId) {
		super();
		this.id = id;
		this.nguoiGuiId = nguoiGuiId;
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
		this.nguoiNhanId = nguoiNhanId;
		this.tenNguoiNhanKhach = tenNguoiNhanKhach;
		this.noiDung = noiDung;
		this.noiBo = noiBo;
		this.hanhDong = hanhDong;
		this.roomId = roomId;
		this.thoiGianGui = thoiGianGui;
		this.nhanVienPhuTrachId = nhanVienPhuTrachId;
	}
	public ChatMessageResponse() {
		super();
	}
}
