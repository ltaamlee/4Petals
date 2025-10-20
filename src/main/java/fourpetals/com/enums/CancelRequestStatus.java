package fourpetals.com.enums;

public enum CancelRequestStatus {
	NONE("None"),      // Không có yêu cầu
    PENDING("Chờ phê duyệt"),   // Người bán gửi yêu cầu chờ phê duyệt
    APPROVED("Duyệt"),  // Quản lý duyệt
    REJECTED("Từ chối");
	
	private final String displayName;

	private CancelRequestStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}	
	
}
