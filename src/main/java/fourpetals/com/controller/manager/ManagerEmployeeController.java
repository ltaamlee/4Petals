package fourpetals.com.controller.manager;

import fourpetals.com.entity.Employee;
import fourpetals.com.enums.EmployeePosition;
import fourpetals.com.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/manager/employees")
public class ManagerEmployeeController {

    private final EmployeeService employeeService;

    public ManagerEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /* LIST */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Quản lý Nhân viên");
        model.addAttribute("employees", employeeService.findAll());
        return "manager/employee/list";
    }

    /* ADD (GET) */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("pageTitle", "Thêm Nhân viên");
        model.addAttribute("employee", new Employee());
        model.addAttribute("positions", EmployeePosition.values());
        return "manager/employee/add";
    }

    /* ADD (POST) */
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("employee") Employee form,
                       BindingResult br, Model model, RedirectAttributes ra,
                       jakarta.servlet.http.HttpServletRequest req) {
    	/*
      log.info("raw chucVu    = `{}`", req.getParameter("chucVu"));
      log.info("form.chucVu   = `{}`", form.getChucVu()); // ENUM sau binding
*/
        // validate thủ công email trùng (nếu cần)
        if (!br.hasErrors() && employeeService.existsByEmail(form.getEmail())) {
            br.rejectValue("email", "email.exists", "Email đã tồn tại");
        }

        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm Nhân viên");
            model.addAttribute("positions", EmployeePosition.values());
            return "manager/employee/add";
        }

        employeeService.save(form);
        ra.addFlashAttribute("success", "Đã thêm nhân viên: " + form.getHoTen());
        return "redirect:/manager/employees";
    }
}
