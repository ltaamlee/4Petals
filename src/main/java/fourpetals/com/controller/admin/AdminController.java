package fourpetals.com.controller.admin;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;
import fourpetals.com.utils.Upload;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private Upload upload;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		model.addAttribute("totalUsers", 100);
		model.addAttribute("todayOrders", 25);
		model.addAttribute("revenue", 5000000);

		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}

		return "admin/dashboard";
	}

	@GetMapping("/profile")
	public String showProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		String username = userDetails.getUsername();

		Optional<User> userOpt = userService.findByUsername(username);
		if (userOpt.isPresent()) {
			model.addAttribute("user", userOpt.get());
		} else {
			model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
			return "admin/error";
		}

		return "admin/profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(required = false) String email, @RequestParam(required = false) String password,
			@RequestParam(required = false) String name, @RequestParam(required = false) String phone,
			@RequestParam(required = false) MultipartFile file, RedirectAttributes redirectAttributes) {

		try {
			String username = userDetails.getUsername();
			Optional<User> userOpt = userService.findByUsername(username);

			if (!userOpt.isPresent()) {
				redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng!");
				redirectAttributes.addFlashAttribute("messageType", "error");
				return "redirect:/admin/profile";
			}

			User user = userOpt.get();

			// Cập nhật email
			if (email != null && !email.trim().isEmpty()) {
				user.setEmail(email.trim());
			}

			// Cập nhật mật khẩu
			if (password != null && !password.trim().isEmpty()) {
				userService.changePassword(user, password);
			}

			// Cập nhật ảnh đại diện
			if (file != null && !file.isEmpty()) {
				if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
					upload.deleteFile(user.getImageUrl());
				}
				String imagePath = upload.saveFile(file, "profile/admin");
				user.setImageUrl(imagePath);
			}

			// Cập nhật Employee (họ tên và số điện thoại)
			if (user.getNhanVien() != null) {
				if (name != null && !name.trim().isEmpty()) {
					user.getNhanVien().setHoTen(phone.trim());
				}
				if (phone != null && !phone.trim().isEmpty()) {
					user.getNhanVien().setSdt(phone.trim());
				}
			}

			// Lưu lại User cùng Employee
			userService.updateUser(user);

			redirectAttributes.addFlashAttribute("message", "Cập nhật profile thành công!");
			redirectAttributes.addFlashAttribute("messageType", "success");

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("message", "Lỗi khi upload ảnh: " + e.getMessage());
			redirectAttributes.addFlashAttribute("messageType", "error");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "Cập nhật thất bại: " + e.getMessage());
			redirectAttributes.addFlashAttribute("messageType", "error");
		}

		return "redirect:/admin/profile";
	}

}