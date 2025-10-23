package fourpetals.com.enums;

public enum PromotionType {
	PERCENT("Giảm theo %"),
    GIFT("Tặng quà");


    private final String displayName;

    PromotionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
