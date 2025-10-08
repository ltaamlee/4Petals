package fourpetals.com.enums;

public enum EmployeePosition {
    MANAGER("Quản lý"),
    SALES_EMPLOYEE("Nhân viên bán hàng"),
    INVENTORY_EMPLOYEE("Nhân viên kho"),
    SHIPPER("Nhân viên giao hàng");

    private final String displayName;

    EmployeePosition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isManager() {
        return this == MANAGER;
    }

    public boolean isSalesRelated() {
        return this == SALES_EMPLOYEE || this == MANAGER;
    }

    public boolean isInventoryRelated() {
        return this == INVENTORY_EMPLOYEE;
    }
}