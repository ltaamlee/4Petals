package fourpetals.com.entity;

import java.time.LocalDateTime;

import fourpetals.com.enums.UserStatus;
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


    @Column(name = "PasswordHash", nullable = false, length = 255)
    private String passwordHash;

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
    
    @OneToOne(mappedBy = "user")
    private Customer khachHang;
    
    @OneToOne(mappedBy = "user")
    private Employee nhanVien;

    @Transient
    public UserStatus getUserStatus() {
        return UserStatus.fromValue(this.status);
    }

    public void setUserStatus(UserStatus userStatus) {
        this.status = userStatus.getValue();
    }
}