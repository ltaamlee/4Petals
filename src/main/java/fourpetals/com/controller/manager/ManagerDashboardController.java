package fourpetals.com.controller.manager;

import fourpetals.com.dto.response.dashboard.*;
import fourpetals.com.service.ManagerDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/manager/dashboard")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerDashboardController {

    @Autowired
    private ManagerDashboardService dashboardService;

    // 4 cards tổng quan
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    // dữ liệu chart theo loại + range
    @GetMapping("/chart")
    public ChartDataResponse getChart(
            @RequestParam String type,            // revenue | employees | customers | orders
            @RequestParam String range,           // month | quarter | year | custom
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return dashboardService.getChart(type, range, start, end);
    }

    // top N sản phẩm bán chạy
    @GetMapping("/top-products")
    public List<TopProductResponse> getTopProducts(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "month") String range,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return dashboardService.getTopProducts(limit, range, start, end);
    }

    // đơn hàng gần đây
    @GetMapping("/recent-orders")
    public List<RecentOrderResponse> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return dashboardService.getRecentOrders(limit);
    }
}
