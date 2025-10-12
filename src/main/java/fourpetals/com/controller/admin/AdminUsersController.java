package fourpetals.com.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.dto.response.users.UserStatsResponse;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.mapper.UserMapping;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.UserService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	// -------------------- Thống kê user --------------------
	@GetMapping("/stats")
	public Object getUserStats() {
		long total = userService.countAllUsers();
		long active = userService.countByStatus(UserStatus.ACTIVE);
		long inactive = userService.countByStatus(UserStatus.INACTIVE);
		long blocked = userService.countByStatus(UserStatus.BLOCKED);

		return new UserStatsResponse(total, active, inactive, blocked);
	}

	// -------------------- Lấy danh sách user --------------------
	@GetMapping
	public Page<UserDetailResponse> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) String status, @RequestParam(required = false) Integer roleId) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
		Page<User> usersPage = userService.searchUsers(keyword, status, roleId, pageable);
		Page<UserDetailResponse> responsePage = usersPage.map(UserMapping::toUserResponse);
		return responsePage;

	}

	// -------------------- Cập nhật trạng thái người dùng --------------------
	@PutMapping("/{userId}")
	public UserDetailResponse updateUserStatus(@PathVariable Integer userId, @RequestBody StatusUpdateRequest request) {
		User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

		if (request.getStatus() != 1 && request.getStatus() != 0 && request.getStatus() != -1) {
			throw new RuntimeException("Trạng thái không hợp lệ");
		}

		user.setUserStatus(UserStatus.fromValue(request.getStatus()));
		userService.updateUser(user);

		return UserMapping.toUserResponse(user);
	}

	public static class StatusUpdateRequest {
		private int status; // 1 = ACTIVE, 0 = INACTIVE, -1 = BLOCKED

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

	// -------------------- Export CSV --------------------
	@GetMapping("/export")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		String filename = "users.csv";

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		List<User> users = userService.findAll();

		PrintWriter writer = response.getWriter();
		writer.println("ID,Tên đăng nhập,Họ tên,Email,SĐT,Vai trò,Ngày tạo,Trạng thái");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		for (User user : users) {
			String hoTen = user.getKhachHang() != null ? user.getKhachHang().getHoTen()
					: (user.getNhanVien() != null ? user.getNhanVien().getHoTen() : "");
			String sdt = user.getKhachHang() != null ? user.getKhachHang().getSdt()
					: (user.getNhanVien() != null ? user.getNhanVien().getSdt() : "");
			String role = user.getRole() != null ? user.getRole().getRoleName().getDisplayName() : "";
			String statusStr = user.getStatus() == 1 ? "Hoạt động"
					: (user.getStatus() == 0 ? "Ngừng hoạt động" : "Bị khóa");

			writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n", user.getUserId(), user.getUsername(), hoTen, user.getEmail(),
					sdt, role, user.getCreatedAt().format(formatter), statusStr);
		}

		writer.flush();
		writer.close();
	}

}