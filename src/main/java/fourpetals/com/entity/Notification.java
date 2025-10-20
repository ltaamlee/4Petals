package fourpetals.com.entity;

import java.time.LocalDateTime;

import fourpetals.com.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="ThongBao")
public class Notification {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTB", nullable = false)
    private Integer maTB;
	
	@Column(name = "NguoiGuiId", nullable = false)
    private Integer nguoiGuiId; 

    @Column(name = "NguoiNhanId", nullable = false)
    private Integer nguoiNhanId; 

    @Column(name = "DonHangId")
    private Integer donHangId;  

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiThongBao", nullable = false)
    private NotificationType loaiThongBao;

    @Column(name = "NoiDung", columnDefinition = "nvarchar(MAX)", nullable = false)
    private String noiDung; 

    @Column(name = "DaDoc", nullable = false)
    private boolean daDoc = false;

    @Column(name = "NgayTao", nullable = false)
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

	public Integer getMaTB() {
		return maTB;
	}

	public void setMaTB(Integer maTB) {
		this.maTB = maTB;
	}

	public Integer getNguoiNhanId() {
		return nguoiNhanId;
	}

	public void setNguoiNhanId(Integer nguoiNhanId) {
		this.nguoiNhanId = nguoiNhanId;
	}

	public Integer getDonHangId() {
		return donHangId;
	}

	public void setDonHangId(Integer donHangId) {
		this.donHangId = donHangId;
	}

	public NotificationType getLoaiThongBao() {
		return loaiThongBao;
	}

	public void setLoaiThongBao(NotificationType loaiThongBao) {
		this.loaiThongBao = loaiThongBao;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public boolean isDaDoc() {
		return daDoc;
	}

	public void setDaDoc(boolean daDoc) {
		this.daDoc = daDoc;
	}

	public LocalDateTime getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDateTime ngayTao) {
		this.ngayTao = ngayTao;
	}

	public Integer getNguoiGuiId() {
		return nguoiGuiId;
	}

	public void setNguoiGuiId(Integer nguoiGuiId) {
		this.nguoiGuiId = nguoiGuiId;
	}

	public Notification(Integer maTB, Integer nguoiGuiId, Integer nguoiNhanId, Integer donHangId,
			NotificationType loaiThongBao, String noiDung, boolean daDoc, LocalDateTime ngayTao) {
		super();
		this.maTB = maTB;
		this.nguoiGuiId = nguoiGuiId;
		this.nguoiNhanId = nguoiNhanId;
		this.donHangId = donHangId;
		this.loaiThongBao = loaiThongBao;
		this.noiDung = noiDung;
		this.daDoc = daDoc;
		this.ngayTao = ngayTao;
	}

	public Notification() {
		super();
	}
   
	
    
    
}
