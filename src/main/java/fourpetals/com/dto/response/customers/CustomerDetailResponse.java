// fourpetals/com/dto/response/customers/CustomerDetailResponse.java
package fourpetals.com.dto.response.customers;

import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerDetailResponse(
        Integer maKH, String hoTen, String sdt, String email,
        Gender gioiTinh, CustomerRank hangThanhVien,
        LocalDate ngaySinh, String diaChi,
        LocalDateTime ngayTao, long tongDon
) {}
