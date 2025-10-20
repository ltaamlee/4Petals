package fourpetals.com.dto.request.supplier;

import java.util.List;

import fourpetals.com.enums.SupplierStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupplierRequest {

    private Integer maNCC;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Size(min = 3, max = 100, message = "Tên nhà cung cấp tối thiểu 3 ký tự và không được vượt quá 100 ký tự")
    private String tenNCC;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String diaChi;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    private SupplierStatus trangThai;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String moTa;

    @NotEmpty(message = "Danh sách nguyên liệu chưa được chọn")
    private List<Integer> nhaCungCapNguyenLieu;

    public SupplierRequest() {
        super();
    }

    public SupplierRequest(Integer maNCC, String tenNCC, String diaChi, String sdt, String email,
                           SupplierStatus trangThai, String moTa, List<Integer> nhaCungCapNguyenLieu) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
    }

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

    public List<Integer> getNhaCungCapNguyenLieu() {
        return nhaCungCapNguyenLieu;
    }

    public void setNhaCungCapNguyenLieu(List<Integer> nhaCungCapNguyenLieu) {
        this.nhaCungCapNguyenLieu = nhaCungCapNguyenLieu;
    }
}
