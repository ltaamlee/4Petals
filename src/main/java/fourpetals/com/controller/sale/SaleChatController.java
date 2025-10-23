package fourpetals.com.controller.sale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.response.ChatMessageResponse;
import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.ChatMessageService;
import fourpetals.com.service.UserService;

@RestController
public class SaleChatController {

	@Autowired
	private UserService userService;

	@Autowired
	private ChatMessageService chatMessageService;

	/**
	 * Lấy thông tin nhân viên sale đang đăng nhập
	 */
	@GetMapping("/api/sale/employee/me")
	public User getCurrentEmployee(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails != null) {
			return userService.findByUsername(userDetails.getUsername()).orElse(null);
		}
		return null;
	}

	/**
	 * Lấy tin nhắn trong phòng của nhân viên sale
	 */
	@GetMapping("/api/sale/employee/{nhanVienId}/messages")
	public List<ChatMessageResponse> getMessagesByNhanVien(@PathVariable Integer nhanVienId) {
		return chatMessageService.getMessagesByNhanVienPhuTrach(nhanVienId);
	}

	/**
	 * Lấy tin nhắn tất cả khách hàng
	 */
	@GetMapping("/api/sale/messages/customers")
	public List<ChatMessageResponse> getCustomerMessages() {
		return chatMessageService.getCustomerMessages();
	}

	/**
	 * Lấy tin nhắn nội bộ (nếu cần)
	 */
	@GetMapping("/api/sale/messages/internal")
	public List<ChatMessageResponse> getInternalMessages() {
		return chatMessageService.getInternalMessages();
	}
}
