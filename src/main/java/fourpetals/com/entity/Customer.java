package fourpetals.com.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fourpetals.com.enums.CustomerRank;
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
@Table(name = "KhachHang")
public class Customer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKH")
    private Integer maKH;

    @Column(name = "HoTen", nullable = false, columnDefinition = "nvarchar(100)")
    private String hoTen;
    

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;
    
    @Enumerated(EnumType.STRING)
    private Gender gioiTinh;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DiaChi",columnDefinition = "nvarchar(100)", length = 200)
    private String diaChi;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "HangThanhVien") 
    private CustomerRank hangThanhVien = CustomerRank.THUONG; 
    
    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @JsonBackReference
    private User user;

	public Integer getMaKH() {
		return maKH;
	}

	public void setMaKH(Integer maKH) {
		this.maKH = maKH;
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

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public CustomerRank getHangThanhVien() {
		return hangThanhVien;
	}

	public void setHangThanhVien(CustomerRank hangThanhVien) {
		this.hangThanhVien = hangThanhVien;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public Customer() {
		super();
	}

	public Customer(Integer maKH, String hoTen, LocalDate ngaySinh, String sdt, String diaChi, User user) {
		super();
		this.maKH = maKH;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.sdt = sdt;
		this.diaChi = diaChi;
		this.user = user;
	}
	
	
}

