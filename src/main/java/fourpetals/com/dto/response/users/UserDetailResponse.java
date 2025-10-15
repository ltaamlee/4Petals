package fourpetals.com.dto.response.users;

import java.time.LocalDateTime;

import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
	private Integer userId;
	private String username;
	private String email;
	private String imageUrl;
	private String fullName; // Lấy từ Employee hoặc Customer
	private String phone; // Lấy từ Employee hoặc Customer
	private String roleName; // Tên hiển thị của vai trò
	private UserStatus status; // Enum trực tiếp
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// Helper method: lấy chữ hiển thị của status
	public String getStatusDisplay() {
		return status != null ? status.getDisplayName() : "Không xác định";
	}

	// Helper method: lấy value của status để logic (0,1,-1)
	public int getStatusValue() {
		return status != null ? status.getValue() : 0;
	}
}
