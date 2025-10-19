package fourpetals.com.enums;

public enum PromotionStatus {
	ACTIVE("Đang hoạt động"),
    INACTIVE("Chưa kích hoạt"),
    EXPIRED("Đã hết hạn"),
    DISABLED("Bị vô hiệu hóa");

    private final String displayName;

    PromotionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
