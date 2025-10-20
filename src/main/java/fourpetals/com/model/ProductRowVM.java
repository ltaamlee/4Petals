package fourpetals.com.model;

import java.math.BigDecimal;

public record ProductRowVM(
        Integer maSP,
        String tenSP,
        String hinhAnh,
        BigDecimal giaGoc,
        Integer trangThai,
        String trangThaiText,
        Long soLuongDaBan
) {}
