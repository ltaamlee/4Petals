package fourpetals.com.controller.customer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.entity.Customer;
import fourpetals.com.service.CustomerService;

@Controller
public class AccountController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/account")
	public String showAccountPage(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		String username = principal.getName();
		Optional<Customer> customerOpt = customerService.findByUser_Username(username);

		if (customerOpt.isEmpty()) {
			return "redirect:/login";
		}

		Customer customer = customerOpt.get();
		model.addAttribute("customer", customer);
		model.addAttribute("user", customer.getUser()); 

		return "customer/account";
	}

	@GetMapping("/account/avatar")
	public String accountAvatar(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		String username = principal.getName();
		Customer customer = customerService.findByUser_Username(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

		model.addAttribute("customer", customer);
		model.addAttribute("user", customer.getUser()); 
		model.addAttribute("title", "Ảnh đại diện");
		return "customer/avatar";
	}

	// Xử lý upload ảnh đại diện
	@PostMapping("/account/avatar/upload")
	public String uploadAvatar(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
		if (principal == null || file.isEmpty()) {
			return "redirect:/account/avatar";
		}

		String username = principal.getName();

		// Tạo thư mục lưu ảnh
		String uploadDir = "src/main/resources/static/uploads/avatar/";
		File directory = new File(uploadDir);
		if (!directory.exists())
			directory.mkdirs();

		// Lưu file
		String fileName = username + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(uploadDir, fileName);
		Files.write(filePath, file.getBytes());

		// Cập nhật đường dẫn vào bảng User
		String imageUrl = "/uploads/avatar/" + fileName;
		customerService.updateAvatar(username, imageUrl);

		return "redirect:/account/avatar";
	}
}