package fourpetals.com.enums;

public enum EmployeePosition {
    QUAN_LY("Quản lý"),
    NHAN_VIEN_BAN_HANG("Nhân viên bán hàng"),
    NHAN_VIEN_KHO("Nhân viên kho"),
    KE_TOAN("Kế toán"),
    GIAO_HANG("Nhân viên giao hàng");

    private final String displayName;

    EmployeePosition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isManager() {
        return this == QUAN_LY;
    }

    public boolean isSalesRelated() {
        return this == NHAN_VIEN_BAN_HANG || this == QUAN_LY;
    }

    public boolean isInventoryRelated() {
        return this == NHAN_VIEN_KHO;
    }
}