package fourpetals.com.entity;

import fourpetals.com.enums.EmployeePosition;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNV")
    private Integer maNV;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Enumerated(EnumType.STRING)
    @Column(name = "ChucVu", length = 50)
    private EmployeePosition chucVu;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;
}
