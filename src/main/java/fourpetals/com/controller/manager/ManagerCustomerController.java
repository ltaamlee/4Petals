package fourpetals.com.controller.manager;

import fourpetals.com.entity.Customer;
import fourpetals.com.model.CustomerRank;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Controller
@RequestMapping("/manager/customers")
public class ManagerCustomerController {

    private final CustomerService service;

    public ManagerCustomerController(CustomerService service) {
        this.service = service;
    }

    // Danh sách + lọc (rank, month, year) + thống kê
    @GetMapping
    public String list(@RequestParam(value = "rank", required = false) CustomerRank rank,
                       @RequestParam(value = "month", required = false) Integer month,
                       @RequestParam(value = "year", required = false) Integer year,
                       Model model) {

        Month m = (month != null && month >= 1 && month <= 12) ? Month.of(month) : null;

        List<CustomerRowVM> rows = service.list(rank, m, year);
        CustomerStatsVM stats = service.stats(year, m);

        model.addAttribute("pageTitle", "Quản lý Khách hàng");
        model.addAttribute("rows", rows);
        model.addAttribute("rank", rank);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("ranks", CustomerRank.values());
        model.addAttribute("stats", stats);

        return "manager/customer/list";
        // View dùng: templates/manager/customer/list.html
    }

    // Form thêm
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("pageTitle", "Thêm Khách hàng");
        model.addAttribute("customer", new Customer());
        return "manager/customer/add";
        // View: templates/manager/customer/add.html
    }

    // Lưu thêm
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("customer") Customer form,
                       BindingResult br,
                       Model model,
                       RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm Khách hàng");
            return "manager/customer/add";
        }
        service.save(form);
        ra.addFlashAttribute("success", "Đã thêm khách hàng: " + form.getHoTen());
        return "redirect:/manager/customers";
    }

    // Form sửa
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Customer c = service.findById(id).orElseThrow();
        model.addAttribute("pageTitle", "Cập nhật Khách hàng");
        model.addAttribute("customer", c);
        return "manager/customer/edit";
        // View: templates/manager/customer/edit.html
    }

    // Lưu sửa
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("customer") Customer form,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Cập nhật Khách hàng");
            return "manager/customer/edit";
        }
        form.setMaKH(id);
        service.save(form);
        ra.addFlashAttribute("success", "Đã cập nhật khách hàng: " + form.getHoTen());
        return "redirect:/manager/customers";
    }

    // Xoá
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Đã xoá khách hàng");
        return "redirect:/manager/customers";
    }

    // Xuất CSV theo bộ lọc hiện tại
    @GetMapping("/export")
    public void exportCsv(@RequestParam(value = "rank", required = false) CustomerRank rank,
                          @RequestParam(value = "month", required = false) Integer month,
                          @RequestParam(value = "year", required = false) Integer year,
                          HttpServletResponse resp) throws Exception {

        Month m = (month != null && month >= 1 && month <= 12) ? Month.of(month) : null;
        List<CustomerRowVM> rows = service.list(rank, m, year);

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=customers.csv");

        try (PrintWriter w = resp.getWriter()) {
            w.println("MaKH,HoTen,SDT,Email,TongDon,Hang,NgayTao");
            for (CustomerRowVM r : rows) {
                w.printf("%d,%s,%s,%s,%d,%s,%s%n",
                        r.maKH(),
                        csvSafe(r.hoTen()),
                        csvSafe(r.sdt()),
                        csvSafe(r.email()),
                        r.tongDon(),
                        r.hang().display,
                        r.ngayTao() != null ? r.ngayTao() : ""
                );
            }
        }
    }

    private String csvSafe(String s) {
        if (s == null) return "";
        // tránh dấu phẩy phá định dạng
        return s.replace(",", " ");
    }

    // (tuỳ chọn) giá trị mặc định cho year khi không truyền
    @ModelAttribute("defaultYear")
    public Integer defaultYear() {
        return LocalDate.now().getYear();
    }
}
