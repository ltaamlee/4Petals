package fourpetals.com.enums;

public enum PromotionType {
	PERCENT("Giảm theo %"),
    AMOUNT("Giảm theo số tiền"),
    GIFT("Tặng quà"),
    COMBO("Gói sản phẩm giảm giá"),
    BUY_X_GET_Y("Mua X tặng Y");

    private final String displayName;

    PromotionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
