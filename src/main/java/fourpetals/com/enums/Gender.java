package fourpetals.com.enums;

public enum Gender {
	NAM("Nam"), 
	NU("Nữ"), 
	KHAC("Khác");

	private final String displayName;

	
	public String getDisplayName() {
		return displayName;
	}

	Gender(String displayName) {
		this.displayName = displayName;
	}
}
