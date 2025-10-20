package fourpetals.com.enums;

public enum SupplierStatus {
	ACTIVE("Đang hợp tác"), 
	INACTIVE("Ngừng hợp tác"), 
	SUSPENDED("Tạm khóa");

	public final String displayName;

	public String getDisplayName() {
		return displayName;
	}

	private SupplierStatus(String displayName) {
		this.displayName = displayName;
	}
	
}
