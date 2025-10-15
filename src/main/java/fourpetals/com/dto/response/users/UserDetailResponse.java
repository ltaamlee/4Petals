package fourpetals.com.dto.response.users;

import java.time.LocalDate;
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

    // Thêm ngày sinh và giới tính
    private LocalDate birthDate; // Lấy từ Employee hoặc Customer
    private String gender;       // Lấy từ Employee hoặc Customer

    // --- Các getter/setter ---
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // --- Helper ---
    public String getStatusDisplay() { return status != null ? status.getDisplayName() : "Không xác định"; }
    public int getStatusValue() { return status != null ? status.getValue() : 0; }

    // --- Chuyển từ User entity ---
    public static UserDetailResponse fromUser(User user) {
        UserDetailResponse dto = new UserDetailResponse();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setImageUrl(user.getImageUrl());

        // Lấy fullName, phone, birthDate, gender từ Employee hoặc Customer
        if (user.getNhanVien() != null) {
            dto.setFullName(user.getNhanVien().getHoTen());
            dto.setPhone(user.getNhanVien().getSdt());
            dto.setBirthDate(user.getNhanVien().getNgaySinh());
            dto.setGender(user.getNhanVien().getGioiTinh() != null ? user.getNhanVien().getGioiTinh().name() : null);
        } else if (user.getKhachHang() != null) {
            dto.setFullName(user.getKhachHang().getHoTen());
            dto.setPhone(user.getKhachHang().getSdt());
            dto.setBirthDate(user.getKhachHang().getNgaySinh());
            dto.setGender(user.getKhachHang().getGioiTinh() != null ? user.getKhachHang().getGioiTinh().name() : null);
        }

        // Role
        if (user.getRole() != null && user.getRole().getRoleName() != null) {
            dto.setRoleName(user.getRole().getRoleName().getDisplayName());
        }

        // Status
        dto.setStatus(user.getUserStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}
