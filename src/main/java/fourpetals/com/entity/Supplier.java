package fourpetals.com.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fourpetals.com.enums.SupplierStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "NhaCungCap")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaNCC")
	private Integer maNCC;

	@Column(name = "TenNCC", nullable = false, columnDefinition = "nvarchar(100)")
	private String tenNCC;

	@Column(name = "DiaChi", columnDefinition = "nvarchar(200)")
	private String diaChi;

	@Column(name = "SDT", unique = true, length = 15)
	private String sdt;

	@Column(name = "Email", unique = true, length = 100)
	private String email;

	@Column(name = "TrangThai", nullable = false)
	@Enumerated(EnumType.STRING)
	private SupplierStatus trangThai = SupplierStatus.ACTIVE;

	@Column(name = "MoTa", columnDefinition = "nvarchar(MAX)")
	private String moTa; // Có thể ghi lí do ngừng hợp tác

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

	@OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<SupplierMaterial> nhaCungCapNguyenLieu;

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SupplierStatus getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(SupplierStatus trangThai) {
		this.trangThai = trangThai;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
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

	public List<SupplierMaterial> getNhaCungCapNguyenLieu() {
		return nhaCungCapNguyenLieu;
	}

	public void setNhaCungCapNguyenLieu(List<SupplierMaterial> nhaCungCapNguyenLieu) {
		this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
	}

	public Supplier() {
		super();
	}

	public Supplier(Integer maNCC, String tenNCC, String diaChi, String sdt, String email, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.sdt = sdt;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
