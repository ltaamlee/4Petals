package fourpetals.com.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID")
	private Integer userId;

	@Column(name = "FullName", nullable = false, length = 100)
	private String fullName;

	@Column(name = "Email", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "PasswordHash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "Phone", length = 15)
	private String phone;

	@Column(name = "Address", length = 255)
	private String address;

	@ManyToOne
	@JoinColumn(name = "RoleID")
	private Role role;

	@Column(name = "Status")
	private Integer status = 1; // 1: Active, 0: Inactive, -1: Blocked

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
}
