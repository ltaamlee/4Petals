//package fourpetals.com.controller.admin;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import fourpetals.com.entity.User;
//import fourpetals.com.security.CustomUserDetails;
//import fourpetals.com.service.UserService;
//import fourpetals.com.utils.Upload;
//
//@RestController
//@RequestMapping("/admin/profile")
//public class AdminProfileController {
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private Upload upload;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@PostMapping("/update")
//	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
//			@RequestParam(required = false) String email, @RequestParam(required = false) String password,
//			@RequestParam(required = false) String name, @RequestParam(required = false) String phone,
//			@RequestParam(required = false) MultipartFile file) {
//
//		try {
//			String username = userDetails.getUsername();
//			Optional<User> userOpt = userService.findByUsername(username);
//
//			if (!userOpt.isPresent()) {
//				return ResponseEntity.badRequest().body(new ApiResponse(false, "Không tìm thấy người dùng!"));
//			}
//
//			User user = userOpt.get();
//
//			if (email != null && !email.trim().isEmpty()) {
//				user.setEmail(email.trim());
//			}
//
//			if (password != null && !password.trim().isEmpty()) {
//				userService.changePassword(user, password);
//			}
//
//			if (file != null && !file.isEmpty()) {
//				if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
//					upload.deleteFile(user.getImageUrl());
//				}
//				String imagePath = upload.saveFile(file, "profile/admin");
//				user.setImageUrl(imagePath);
//			}
//
//			if (user.getNhanVien() != null) {
//				if (name != null && !name.trim().isEmpty()) {
//					user.getNhanVien().setHoTen(name.trim());
//				}
//				if (phone != null && !phone.trim().isEmpty()) {
//					user.getNhanVien().setSdt(phone.trim());
//				}
//			}
//
//			userService.updateUser(user);
//
//			return ResponseEntity.ok(new ApiResponse(true, "Cập nhật profile thành công!", user));
//
//		} catch (IOException e) {
//			return ResponseEntity.badRequest().body(new ApiResponse(false, "Lỗi khi upload ảnh: " + e.getMessage()));
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(new ApiResponse(false, "Cập nhật thất bại: " + e.getMessage()));
//		}
//	}
//
//	public static class ApiResponse {
//		private boolean success;
//		private String message;
//		private User user;
//
//		public ApiResponse(boolean success, String message) {
//			this.success = success;
//			this.message = message;
//		}
//
//		public ApiResponse(boolean success, String message, User user) {
//			this.success = success;
//			this.message = message;
//			this.user = user;
//		}
//
//		public boolean isSuccess() {
//			return success;
//		}
//
//		public void setSuccess(boolean success) {
//			this.success = success;
//		}
//
//		public String getMessage() {
//			return message;
//		}
//
//		public void setMessage(String message) {
//			this.message = message;
//		}
//
//		public User getUser() {
//			return user;
//		}
//
//		public void setUser(User user) {
//			this.user = user;
//		}
//	}
//}
