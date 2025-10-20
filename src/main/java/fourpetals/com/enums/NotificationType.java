package fourpetals.com.enums;

public enum NotificationType {
	CANCEL_REQUEST("Yêu cầu hủy đơn"),     
    ORDER_STATUS_UPDATE("Cập nhật trạng thái đơn hàng"), 
    NEW_ORDER("Đơn hàng mới"),         
    CHAT_MESSAGE("Tin nhắn"),       
    OTHER("Khác");          
	private final String displayName;

	public String getDisplayName() {
		return displayName;
	}

	private NotificationType(String displayName) {
		this.displayName = displayName;
	}

}
