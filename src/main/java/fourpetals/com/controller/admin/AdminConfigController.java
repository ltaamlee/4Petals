//package fourpetals.com.controller.admin;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import fourpetals.com.entity.PaymentConfig;
//import fourpetals.com.entity.User;
//import fourpetals.com.enums.PaymentMethod;
//import fourpetals.com.security.CustomUserDetails;
//import fourpetals.com.service.UserService;
//
//@Controller
//@RequestMapping("/admin/config")
//public class AdminConfigController {
//
//	@Autowired
//	private UserService userService;
//
//	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
//	public String getConfig(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//
//		if (userDetails != null) {
//			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
//			userOpt.ifPresent(user -> model.addAttribute("user", user));
//		}
//
//		// ====== Giả lập tên cửa hàng ======
//		String shopName = "Cửa hàng Bốn Cánh Hoa 🌸";
//		model.addAttribute("shopName", shopName);
//
//		// ====== Giả lập thông báo ======
//		model.addAttribute("message", "Cấu hình được tải thành công!");
//		model.addAttribute("messageType", "success");
//
//		// ====== Giả lập cấu hình thanh toán ======
//		PaymentConfig config = new PaymentConfig();
//		config.setCodEnabled(true);
//		config.setCodDescription("Thanh toán khi nhận hàng (COD).");
//
//		config.setBankTransferEnabled(true);
//		config.setBankAccountName("CÔNG TY TNHH BỐN CÁNH HOA");
//		config.setBankAccountNumber("123456789012");
//		config.setBankName("Vietcombank");
//		config.setBankBranch("Chi nhánh Hà Nội");
//		config.setBankTransferContent("[MADON] - [HOTEN] - [SODIENTHOAI]");
//
//		config.setMomoEnabled(true);
//		config.setMomoPartnerCode("MOMOTEST123");
//		config.setMomoAccessKey("ACCESSKEY123");
//		config.setMomoSecretKey("SECRETKEY123");
//		config.setMomoEndpoint("https://test-payment.momo.vn/v2/gateway/api/create");
//
//		config.setVnpayEnabled(true);
//		config.setVnpayMerchantId("VNPAY123");
//		config.setVnpayHashSecret("HASHSECRET123");
//		config.setVnpayApiUrl("https://sandbox.vnpayment.vn/paygate/api/transaction");
//		config.setVnpayReturnUrl("https://yourdomain.com/payment/vnpay/return");
//
//		config.setZalopayEnabled(true);
//		config.setZalopayAppId("ZALOAPP123");
//		config.setZalopayKey1("ZALO_KEY1_ABC");
//		config.setZalopayKey2("ZALO_KEY2_DEF");
//		config.setZalopayEndpoint("https://sandbox.zalopay.com.vn/api/v2/create");
//
//		config.setPaypalEnabled(true);
//		config.setPaypalClientId("PAYPAL_CLIENT_ID_123");
//		config.setPaypalSecretKey("PAYPAL_SECRET_456");
//		config.setPaypalMode("SANDBOX");
//
//		model.addAttribute("config", config);
//
//		return "admin/config";
//	}
//}
