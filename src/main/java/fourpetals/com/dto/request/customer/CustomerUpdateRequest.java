package fourpetals.com.dto.request.customer;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
    @NotBlank @Size(max=100) String hoTen,
    LocalDate ngaySinh,
    @Pattern(regexp = "^(NAM|NU|KHAC)?$") String gioiTinh, 
    @Size(max=15) String sdt,
    @Size(max=200) String diaChi,
    String hang 
) {}