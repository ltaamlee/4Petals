package fourpetals.com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhMuc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaDM")
	private Integer maDM;

	@Column(name = "TenDM", nullable = false, columnDefinition = "nvarchar(100)")
	private String tenDM;

	@Column(name = "MoTa", columnDefinition = "nvarchar(255)")
	private String moTa;

	@Column(name = "UpdatedAt")
	private LocalDateTime updatedAt;

	@PrePersist
	@PreUpdate
	public void touch() {
		this.updatedAt = LocalDateTime.now();
	}

	public Integer getMaDM() {
		return maDM;
	}

	public void setMaDM(Integer maDM) {
		this.maDM = maDM;
	}

	public String getTenDM() {
		return tenDM;
	}

	public void setTenDM(String tenDM) {
		this.tenDM = tenDM;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
