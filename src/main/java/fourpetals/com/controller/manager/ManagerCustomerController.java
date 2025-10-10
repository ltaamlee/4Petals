// src/main/java/fourpetals/com/controller/manager/ManagerCustomerController.java
package fourpetals.com.controller.manager;

import fourpetals.com.entity.Customer;
import fourpetals.com.model.CustomerRank;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.List;

@Controller
@RequestMapping("/manager/customers")
public class ManagerCustomerController {

    private final CustomerService service;

    public ManagerCustomerController(CustomerService service) {
        this.service = service;
    }

    // Danh sách + Tìm kiếm (q) + Lọc tháng/năm + Thống kê
    @GetMapping
    public String list(@RequestParam(value = "q",     required = false) String q,
                       @RequestParam(value = "month", required = false) Integer month,
                       @RequestParam(value = "year",  required = false) Integer year,
                       Model model) {
        Month m = (month != null && month >= 1 && month <= 12) ? Month.of(month) : null;

        List<CustomerRowVM> rows = service.listWithSearch(q, m, year);   // <— thêm hàm này
        CustomerStatsVM stats = service.stats(year, m);

        model.addAttribute("pageTitle", "Quản lý Khách hàng");
        model.addAttribute("rows", rows);
        model.addAttribute("q", q);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("stats", stats);
        return "manager/customer/list";
    }

    // Trang chi tiết
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Customer c = service.findById(id).orElseThrow();
        // Tận dụng RowVM để show tổng quan
        CustomerRowVM vm = service.oneRow(id);
        model.addAttribute("pageTitle", "Chi tiết khách hàng");
        model.addAttribute("customer", c);
        model.addAttribute("row", vm);
        return "manager/customer/detail";
    }

    // Form sửa
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Customer c = service.findById(id).orElseThrow();
        model.addAttribute("pageTitle", "Cập nhật Khách hàng");
        model.addAttribute("customer", c);
        return "manager/customer/edit";
    }

    // Lưu sửa
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id, @ModelAttribute("customer") Customer form) {
        form.setMaKH(id);
        service.save(form);
        return "redirect:/manager/customers/" + id; // về trang chi tiết cho mượt
    }
}
