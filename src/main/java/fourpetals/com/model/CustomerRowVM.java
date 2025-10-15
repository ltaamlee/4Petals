// fourpetals/com/model/CustomerRowVM.java
package fourpetals.com.model;

import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import java.time.LocalDateTime;

public record CustomerRowVM(
        Integer maKH, String hoTen, String sdt, String email,
        Gender gioiTinh, CustomerRank hangThanhVien,
        long tongDon, LocalDateTime ngayTao
) {}
