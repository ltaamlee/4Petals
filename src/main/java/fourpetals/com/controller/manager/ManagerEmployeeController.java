package fourpetals.com.controller.manager;

import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.mapper.UserMapping;
import fourpetals.com.service.EmployeeService;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RestController
@RequestMapping("/api/manager/employees")
public class ManagerEmployeeController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private EmployeeService employeeService;


	// -------------------- Thống kê nhân viên --------------------
//	@GetMapping("/stats")
//	public Object getEmployeeStats() {
//		long total = userService.countAllUsers();
//		long active = userService.countByStatus(UserStatus.ACTIVE);
//		long inactive = userService.countByStatus(UserStatus.INACTIVE);
//		long blocked = userService.countByStatus(UserStatus.BLOCKED);
//
//		return new UserStatsResponse(total, active, inactive, blocked);
//	}

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

//	/* DETAIL */
//	@GetMapping("/{id}")
//	public String detail(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
//		Employee e = employeeService.findById(id);
//		if (e == null) {
//			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
//			return "redirect:/manager/employees";
//		}
//		model.addAttribute("pageTitle", "Chi tiết nhân viên");
//		model.addAttribute("employee", e);
//		return "manager/employee/detail";
//	}
//
//	/* EDIT (GET) */
//	@GetMapping("/{id}/edit")
//	public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
//		Employee e = employeeService.findById(id);
//		if (e == null) {
//			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
//			return "redirect:/manager/employees";
//		}
//		model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
//		model.addAttribute("employee", e);
//		model.addAttribute("positions", EmployeePosition.values());
//		return "manager/employee/edit";
//	}
//
//	/* UPDATE (POST) */
//	@PostMapping("/{id}/edit")
//	public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("employee") Employee form,
//			BindingResult br, Model model, RedirectAttributes ra) {
//
//		Employee current = employeeService.findById(id);
//		if (current == null) {
//			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
//			return "redirect:/manager/employees";
//		}
//
//		if (!br.hasErrors() && form.getEmail() != null && employeeService.existsByEmailExceptId(form.getEmail(), id)) {
//			br.rejectValue("email", "email.exists", "Email đã tồn tại");
//		}
//
//		if (br.hasErrors()) {
//			model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
//			model.addAttribute("positions", EmployeePosition.values());
//			return "manager/employee/edit";
//		}
//
//		current.setHoTen(form.getHoTen());
//		current.setSdt(form.getSdt());
//		current.setEmail(form.getEmail());
//		current.setChucVu(form.getChucVu());
//
//		employeeService.save(current);
//		ra.addFlashAttribute("success", "Đã cập nhật nhân viên: " + current.getHoTen());
//		return "redirect:/manager/employees/" + id;
//	}
}