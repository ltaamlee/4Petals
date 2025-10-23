package fourpetals.com.entity;

import java.time.LocalDateTime;

import fourpetals.com.enums.ChatAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "TinNhan")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Người gửi: nhân viên hoặc khách có tài khoản
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NguoiGuiID")
	private User nguoiGui; // null nếu khách không có tài khoản

	@Column(name = "TenNguoiGuiKhach")
	private String tenNguoiGuiKhach; // khách không tài khoản

	// Người nhận: nhân viên hoặc khách có tài khoản
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NguoiNhanID")
	private User nguoiNhan; // null nếu khách không có tài khoản

	@Column(name = "TenNguoiNhanKhach")
	private String tenNguoiNhanKhach;

	@Column(nullable = false)
	private String noiDung;

	@Enumerated(EnumType.STRING)
	private ChatAction hanhDong;

	@Column(nullable = false)
	private boolean noiBo; // true = chat nội bộ, false = chat khách

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RoomID")
	private ChatRoom chatRoom;

	// Nhân viên phụ trách ca trực (chỉ áp dụng chat khách)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaNV")
	private User nhanVienPhuTrach; // null nếu chat nội bộ

	private LocalDateTime thoiGianGui;

	@PrePersist
	protected void onCreate() {
		thoiGianGui = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getNguoiGui() {
		return nguoiGui;
	}

	public void setNguoiGui(User nguoiGui) {
		this.nguoiGui = nguoiGui;
	}

	public String getTenNguoiGuiKhach() {
		return tenNguoiGuiKhach;
	}

	public void setTenNguoiGuiKhach(String tenNguoiGuiKhach) {
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
	}

	public User getNguoiNhan() {
		return nguoiNhan;
	}

	public void setNguoiNhan(User nguoiNhan) {
		this.nguoiNhan = nguoiNhan;
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

	public ChatAction getHanhDong() {
		return hanhDong;
	}

	public void setHanhDong(ChatAction hanhDong) {
		this.hanhDong = hanhDong;
	}

	public boolean isNoiBo() {
		return noiBo;
	}

	public void setNoiBo(boolean noiBo) {
		this.noiBo = noiBo;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public User getNhanVienPhuTrach() {
		return nhanVienPhuTrach;
	}

	public void setNhanVienPhuTrach(User nhanVienPhuTrach) {
		this.nhanVienPhuTrach = nhanVienPhuTrach;
	}

	public LocalDateTime getThoiGianGui() {
		return thoiGianGui;
	}

	public void setThoiGianGui(LocalDateTime thoiGianGui) {
		this.thoiGianGui = thoiGianGui;
	}

	public ChatMessage(Integer id, User nguoiGui, String tenNguoiGuiKhach, User nguoiNhan, String tenNguoiNhanKhach,
			String noiDung, ChatAction hanhDong, boolean noiBo, ChatRoom chatRoom, User nhanVienPhuTrach,
			LocalDateTime thoiGianGui) {
		super();
		this.id = id;
		this.nguoiGui = nguoiGui;
		this.tenNguoiGuiKhach = tenNguoiGuiKhach;
		this.nguoiNhan = nguoiNhan;
		this.tenNguoiNhanKhach = tenNguoiNhanKhach;
		this.noiDung = noiDung;
		this.hanhDong = hanhDong;
		this.noiBo = noiBo;
		this.chatRoom = chatRoom;
		this.nhanVienPhuTrach = nhanVienPhuTrach;
		this.thoiGianGui = thoiGianGui;
	}

	public ChatMessage() {
		super();
	}

}
