package fourpetals.com.model;

import java.util.Map;

public record CustomerStatsVM(
        long tongKhachHang,
        long khachHangMoiTrongThang,
        Map<Integer, Long> newByMonth
) {}
