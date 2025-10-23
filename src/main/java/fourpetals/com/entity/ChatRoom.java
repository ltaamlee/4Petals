package fourpetals.com.entity;

import java.time.LocalDateTime;
import java.util.List;

import fourpetals.com.enums.RoomType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="PhongChat")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// RoomId dùng làm topic WebSocket, ví dụ: guest-{sessionId}, customer-{userId},
	// internal-{teamId}
	@Column(nullable = false, unique = true)
	private String roomId;

	private String moTa; // mô tả room (tùy chọn)

	@Enumerated(EnumType.STRING)
	private RoomType loaiRoom; // GUEST, CUSTOMER, EMPLOYEE_INTERNAL

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ChatMessage> tinNhans;


	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public RoomType getLoaiRoom() {
		return loaiRoom;
	}

	public void setLoaiRoom(RoomType loaiRoom) {
		this.loaiRoom = loaiRoom;
	}

	public List<ChatMessage> getTinNhans() {
		return tinNhans;
	}

	public void setTinNhans(List<ChatMessage> tinNhans) {
		this.tinNhans = tinNhans;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ChatRoom(Integer id, String roomId, String moTa, RoomType loaiRoom, List<ChatMessage> tinNhans,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.roomId = roomId;
		this.moTa = moTa;
		this.loaiRoom = loaiRoom;
		this.tinNhans = tinNhans;
		this.createdAt = createdAt;
	}

	public ChatRoom() {
		super();
	}
	
	
	

}
