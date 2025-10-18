package fourpetals.com.enums;

public enum ShippingFee {
	NOI_THANH("Nội thành"), NGOAI_THANH("Ngoại thành");

	private final String displayName;

	ShippingFee(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
