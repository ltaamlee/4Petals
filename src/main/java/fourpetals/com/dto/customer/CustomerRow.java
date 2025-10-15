// fourpetals/com/dto/customer/CustomerRow.java
package fourpetals.com.dto.customer;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;

public record CustomerRow(
        Integer maKH,
        String hoTen,
        String sdt,
        String email,
        Gender gioiTinh,
        CustomerRank hangThanhVien,
        long tongDon,
        LocalDateTime ngayTao
) { }
