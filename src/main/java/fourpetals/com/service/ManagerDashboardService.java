package fourpetals.com.service;

import fourpetals.com.dto.response.dashboard.*;
import java.time.LocalDate;
import java.util.List;

public interface ManagerDashboardService {
    DashboardSummaryResponse getSummary();
    ChartDataResponse getChart(String type, String range, LocalDate start, LocalDate end);
    List<TopProductResponse> getTopProducts(int limit, String range, LocalDate start, LocalDate end);
    List<RecentOrderResponse> getRecentOrders(int limit);
}
