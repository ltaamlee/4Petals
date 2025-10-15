package fourpetals.com.controller.manager;

import fourpetals.com.dto.response.customers.CustomerDetailResponse;
import fourpetals.com.dto.response.customers.CustomerStatsResponse;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.Series;
import fourpetals.com.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/manager/customers")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerCustomersController {

    @Autowired private CustomerService customerService;

    // Stats cho cards + chart
    @GetMapping("/stats")
    public Object stats(@RequestParam(defaultValue = "month") String range,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        Series s = customerService.getNewCustomersSeries(range, start, end);
        long male = customerService.countByGender(Gender.NAM);
        long female = customerService.countByGender(Gender.NU);
        long total = customerService.countAll();
        long newMonth = customerService.countNewInCurrentMonth();

        record Payload(java.util.List<String> labels, java.util.List<Integer> values,
                       long maleCount, long femaleCount, CustomerStatsResponse cards) {}
        return new Payload(s.labels(), s.values(), male, female,
                new CustomerStatsResponse(total, newMonth, male, female));
    }

    // Danh sách KH (phân trang)
    @GetMapping
    public Page<CustomerRowVM> list(@RequestParam(defaultValue = "") String keyword,
                                    @RequestParam(required = false) Gender gender,
                                    @RequestParam(required = false) CustomerRank rank,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return customerService.searchRows(keyword, gender, rank, page, size);
    }

    // Chi tiết
    @GetMapping("/{maKH}")
    public CustomerDetailResponse detail(@PathVariable Integer maKH) {
        return customerService.getDetail(maKH)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
    }

   
    
  
    
}
