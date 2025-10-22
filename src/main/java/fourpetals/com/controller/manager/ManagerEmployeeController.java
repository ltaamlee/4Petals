package fourpetals.com.controller.manager;

import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.service.EmployeeService;
import fourpetals.com.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/employees")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerEmployeeController {
	@Autowired
	private EmployeeService service;
	@Autowired
	private RoleService roleService;

	// Danh sách nhân viên (lọc theo keyword/status; auto ẩn ADMIN/MANAGER)
	@GetMapping
	public Page<UserDetailResponse> list(
	        @RequestParam(defaultValue = "") String keyword,
	        @RequestParam(required = false) String status, // "1" | "0" | "-1" | null/""
	        @RequestParam(required = false) Integer roleId, // thêm roleId
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "userId,asc") String sort // FE đang dùng userId
	) {
	    // Map "userId" -> "user.userId" để sort từ root Employee
	    Sort sortSpec;
	    String[] p = sort.split(",");
	    String field = (p.length > 0 ? p[0] : "userId").trim();
	    String dir = (p.length > 1 ? p[1] : "asc").trim();

	    if ("userId".equals(field))
	        field = "user.userId";
	    if ("email".equals(field))
	        field = "user.email";
	    if ("username".equals(field))
	        field = "user.username";
	    // các field thuộc Employee như hoTen, sdt thì giữ nguyên

	    sortSpec = Sort.by(Sort.Direction.fromString(dir), field);
	    Pageable pageable = PageRequest.of(page, size, sortSpec);

	    return service.search(keyword, status, roleId, pageable); // truyền roleId xuống service
	}


	@GetMapping("/view/{employeeId}")
	public UserDetailResponse view(@PathVariable Integer employeeId) {
		return service.findByEmployeeId(employeeId)
				.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
						org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));
	}

	@GetMapping("/stats")
	public UserStatsResponse stats() {
		return service.stats();
	}
}
