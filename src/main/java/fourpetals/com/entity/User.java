package fourpetals.com.entity;
import java.time.LocalDateTime;

import fourpetals.com.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "ImageUrl", length = 255)
    private String imageUrl;

    @Column(name = "Username", nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Role role;

    @Column(name = "Status")
    private Integer status = UserStatus.ACTIVE.getValue(); // Lưu giá trị số: 1, 0, -1

    @Column(name = "Code", length = 50)
    private String code;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer khachHang;

    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Employee nhanVien;

    @Transient
    public UserStatus getUserStatus() {
        return UserStatus.fromValue(this.status);
    }

    public void setUserStatus(UserStatus userStatus) {
        this.status = userStatus.getValue();
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Customer getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(Customer khachHang) {
		this.khachHang = khachHang;
	}

	public Employee getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(Employee nhanVien) {
		this.nhanVien = nhanVien;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}
}