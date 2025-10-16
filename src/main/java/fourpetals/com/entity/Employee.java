package fourpetals.com.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fourpetals.com.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "NhanVien")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaNV")
	private Integer maNV;

	@Column(name = "HoTen", nullable = false, columnDefinition = "nvarchar(100)")
	private String hoTen;
	
    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;
    
	@Enumerated(EnumType.STRING)
	private Gender gioiTinh;

	@Column(name = "SDT", length = 15)
	private String sdt;

	@OneToOne
	@JoinColumn(name = "UserID", referencedColumnName = "UserID")
	@JsonBackReference
	private User user;

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

	public Gender getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(Gender gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Employee() {
		super();
	}

	public Employee(Integer maNV, String hoTen, String sdt, User user) {
		super();
		this.maNV = maNV;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.user = user;
	}

}
