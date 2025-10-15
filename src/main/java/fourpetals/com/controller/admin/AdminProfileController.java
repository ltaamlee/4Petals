package fourpetals.com.controller.admin;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.dto.response.ApiResponse;
import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;
import fourpetals.com.utils.Upload;

@RestController
@RequestMapping("/admin/profile")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfileController {

	@Autowired
	private UserService userService;

	@Autowired
	private Upload upload;

	@PostMapping("/update")
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(required = false) String email, @RequestParam(required = false) String password,
			@RequestParam(required = false) String name, @RequestParam(required = false) String phone,
			@RequestParam(required = false) MultipartFile file) {

		try {
			String username = userDetails.getUsername();
			Optional<User> userOpt = userService.findByUsername(username);

			if (!userOpt.isPresent()) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Không tìm thấy người dùng!"));
			}

			User user = userOpt.get();

			if (email != null && !email.trim().isEmpty()) {
				user.setEmail(email.trim());
			}

			if (password != null && !password.trim().isEmpty()) {
				userService.changePassword(user, password);
			}

			if (file != null && !file.isEmpty()) {
				if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
					upload.deleteFile(user.getImageUrl());
				}
				String imagePath = upload.saveFile(file, "profile/admin");
				user.setImageUrl(imagePath);
			}

			if (user.getNhanVien() != null) {
				if (name != null && !name.trim().isEmpty()) {
					user.getNhanVien().setHoTen(name.trim());
				}
				if (phone != null && !phone.trim().isEmpty()) {
					user.getNhanVien().setSdt(phone.trim());
				}
			}

			userService.updateUser(user);

			return ResponseEntity.ok(new ApiResponse(true, "Cập nhật profile thành công!", user));

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Lỗi khi upload ảnh: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Cập nhật thất bại: " + e.getMessage()));
		}
	}
}
