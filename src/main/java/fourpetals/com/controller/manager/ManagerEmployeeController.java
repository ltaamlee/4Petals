package fourpetals.com.controller.manager;

import fourpetals.com.entity.Employee;
import fourpetals.com.enums.EmployeePosition;
import fourpetals.com.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/manager/employees")
public class ManagerEmployeeController {

	private final EmployeeService employeeService;

	public ManagerEmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@GetMapping
	public String list(@RequestParam(value = "q", required = false) String q, Model model) {
	    model.addAttribute("pageTitle", "Quản lý Nhân viên");
	    model.addAttribute("q", q);

	    boolean hasQuery = q != null && !q.trim().isEmpty();  // <-- quan trọng
	    model.addAttribute("hasQuery", hasQuery);

	    model.addAttribute("employees", employeeService.searchByName(q));
	    return "manager/employee/list";
	}


	/* DETAIL */
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		Employee e = employeeService.findById(id);
		if (e == null) {
			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
			return "redirect:/manager/employees";
		}
		model.addAttribute("pageTitle", "Chi tiết nhân viên");
		model.addAttribute("employee", e);
		return "manager/employee/detail";
	}

	/* EDIT (GET) */
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		Employee e = employeeService.findById(id);
		if (e == null) {
			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
			return "redirect:/manager/employees";
		}
		model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
		model.addAttribute("employee", e);
		model.addAttribute("positions", EmployeePosition.values());
		return "manager/employee/edit";
	}

	/* UPDATE (POST) */
	@PostMapping("/{id}/edit")
	public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("employee") Employee form,
			BindingResult br, Model model, RedirectAttributes ra) {

		Employee current = employeeService.findById(id);
		if (current == null) {
			ra.addFlashAttribute("error", "Không tìm thấy nhân viên #" + id);
			return "redirect:/manager/employees";
		}

		if (!br.hasErrors() && form.getEmail() != null && employeeService.existsByEmailExceptId(form.getEmail(), id)) {
			br.rejectValue("email", "email.exists", "Email đã tồn tại");
		}

		if (br.hasErrors()) {
			model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
			model.addAttribute("positions", EmployeePosition.values());
			return "manager/employee/edit";
		}

		current.setHoTen(form.getHoTen());
		current.setSdt(form.getSdt());
		current.setEmail(form.getEmail());
		current.setChucVu(form.getChucVu());

		employeeService.save(current);
		ra.addFlashAttribute("success", "Đã cập nhật nhân viên: " + current.getHoTen());
		return "redirect:/manager/employees/" + id;
	}
}