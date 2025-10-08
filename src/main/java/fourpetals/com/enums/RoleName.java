package fourpetals.com.enums;

public enum RoleName {
    ADMIN("Quản trị viên"),
    MANAGER("Người quản lý cửa hàng"),
    SALES_EMPLOYEE("Nhân viên bán hàng"),
    INVENTORY_EMPLOYEE("Nhân viên kho"),
    SHIPPER("Nhân viên giao hàng"),
    CUSTOMER("Khách hàng");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RoleName fromString(String roleName) {
        for (RoleName role : RoleName.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + roleName);
    }
}
