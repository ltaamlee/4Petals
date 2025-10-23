package fourpetals.com.dto.request.users;

import java.time.LocalDate;

import fourpetals.com.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmployeeRequest {
	@NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String hoTen;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate ngaySinh;

    @NotNull(message = "Giới tính không được để trống")
    private Gender gioiTinh;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải đúng 10 chữ số")
    private String sdt;

    @Valid
    @NotNull(message = "Thông tin user không được để trống")
    private UserRequest user;

    @NotNull(message = "Role không được để trống")
    private Integer roleId;

	public Integer getRoleId() {
	    return roleId;
	}
	public void setRoleId(Integer roleId) {
	    this.roleId = roleId;
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
	public UserRequest getUser() {
		return user;
	}
	public void setUser(UserRequest user) {
		this.user = user;
	}
	
}
