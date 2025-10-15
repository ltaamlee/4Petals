package fourpetals.com.dto.response;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailResponse {

	// --- Thông tin từ User ---
	private Integer userId;
	private String username;
	private String email;
	private String imageUrl;
	private String statusName; // e.g., "Hoạt động", "Bị khóa"
	private String roleName; // e.g., "Nhân viên bán hàng"
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// --- Thông tin từ Employee ---
	private Integer maNV; // Employee's own ID
	private String hoTen;
	private LocalDate ngaySinh;
	private String gioiTinh; // Trả về tên hiển thị cho dễ dùng
	private String sdt;

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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public Integer getMaNV() {
		return maNV;
	}

	public void setMaNV(Integer maNV) {
		this.maNV = maNV;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public static EmployeeDetailResponse fromUser(User user) {
		EmployeeDetailResponse dto = new EmployeeDetailResponse();

		// Map thông tin User
		dto.setUserId(user.getUserId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setImageUrl(user.getImageUrl());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());

		if (user.getRole() != null) {
			dto.setRoleName(user.getRole().getRoleName().getDisplayName());
		}

		if (user.getUserStatus() != null) {
			dto.setStatusName(user.getUserStatus().getDisplayName());
		}

		Employee employee = user.getNhanVien();
		if (employee != null) {
			dto.setMaNV(employee.getMaNV());
			dto.setHoTen(employee.getHoTen());
			dto.setNgaySinh(employee.getNgaySinh());
			dto.setSdt(employee.getSdt());

			if (employee.getGioiTinh() != null) {
				dto.setGioiTinh(employee.getGioiTinh().getDisplayName());
			}
		}
		return dto;
	}
}