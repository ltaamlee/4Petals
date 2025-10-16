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
@Table(name = "Chat")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Người gửi: nếu là nhân viên thì map User, nếu là khách thì giữ String
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NguoiGuiID")
	private User nguoiGui; // nhân viên nội bộ gửi

	@Column(name = "NguoiGuiKhach")
	private String nguoiGuiKhach; // tên khách gửi (nếu có)

	// Người nhận: nhân viên nội bộ
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NguoiNhanID")
	private User nguoiNhan; // nhân viên nội bộ nhận

	@Column(name = "NguoiNhanKhach")
	private String nguoiNhanKhach; // tên khách nhận (nếu có)

	private String content;

	@Enumerated(EnumType.STRING)
	private ChatAction hanhDong;

	// true = chat nội bộ, false = chat với khách
	private boolean isNoiBo;

	private LocalDateTime timestamp;

	// Nhân viên phụ trách ca trực
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaNV")
	private User nhanVienPhuTrach;

	@PrePersist
	protected void onCreate() {
		timestamp = LocalDateTime.now();
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

	public String getNguoiGuiKhach() {
		return nguoiGuiKhach;
	}

	public void setNguoiGuiKhach(String nguoiGuiKhach) {
		this.nguoiGuiKhach = nguoiGuiKhach;
	}

	public User getNguoiNhan() {
		return nguoiNhan;
	}

	public void setNguoiNhan(User nguoiNhan) {
		this.nguoiNhan = nguoiNhan;
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getNhanVienPhuTrach() {
		return nhanVienPhuTrach;
	}

	public void setNhanVienPhuTrach(User nhanVienPhuTrach) {
		this.nhanVienPhuTrach = nhanVienPhuTrach;
	}
	
	
}
