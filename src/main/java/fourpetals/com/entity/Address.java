package fourpetals.com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DiaChi")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDC")
    private Integer maDC;

    @Column(name = "HoTen", columnDefinition = "nvarchar(100)")
    private String hoTen;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DiaChiChiTiet", columnDefinition = "nvarchar(max)")
    private String diaChiChiTiet;

    @Column(name = "MacDinh")
    private Boolean macDinh = false; // true = địa chỉ mặc định

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private Customer khachHang;

    // ======== GETTERS & SETTERS ========

    public Integer getMaDC() {
        return maDC;
    }

    public void setMaDC(Integer maDC) {
        this.maDC = maDC;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChiChiTiet() {
        return diaChiChiTiet;
    }

    public void setDiaChiChiTiet(String diaChiChiTiet) {
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public Boolean getMacDinh() {
        return macDinh;
    }

    public void setMacDinh(Boolean macDinh) {
        this.macDinh = macDinh;
    }

    public Customer getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(Customer khachHang) {
        this.khachHang = khachHang;
    }
}
