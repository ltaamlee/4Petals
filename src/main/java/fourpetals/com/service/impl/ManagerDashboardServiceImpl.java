//// fourpetals/com/service/impl/DashboardServiceImpl.java
//package fourpetals.com.service.impl;
//
//import fourpetals.com.dto.response.dashboard.*;
//import fourpetals.com.entity.Order;
//import fourpetals.com.enums.OrderStatus;
//import fourpetals.com.repository.*;
//import fourpetals.com.service.ManagerDashboardService;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ManagerDashboardServiceImpl implements ManagerDashboardService {
//
//    private final OrderRepository orderRepository;
//    private final EmployeeRepository employeeRepository;
//    private final CustomerRepository customerRepository; // nếu có
//    // Nếu không có CustomerRepository, có thể dùng UserRepository lọc role CUSTOMER
//
//    public ManagerDashboardServiceImpl(
//            OrderRepository orderRepository,
//            EmployeeRepository employeeRepository,
//            CustomerRepository customerRepository
//    ) {
//        this.orderRepository = orderRepository;
//        this.employeeRepository = employeeRepository;
//        this.customerRepository = customerRepository;
//    }
//
//    @Override
//    public DashboardSummaryResponse getSummary() {
//        // Doanh thu = sum tongTien (đơn không bị hủy)
//        BigDecimal revenue = orderRepository.sumRevenueExcludeStatus(OrderStatus.HUY.name());
//        if (revenue == null) revenue = BigDecimal.ZERO;
//
//        long totalEmp = employeeRepository.count(); // đã là nhân viên
//        long totalCus = customerRepository.count(); // tuỳ entity của em
//        long totalOrd = orderRepository.count();
//
//        return new DashboardSummaryResponse(revenue, totalEmp, totalCus, totalOrd);
//    }
//
//    @Override
//    public ChartDataResponse getChart(String type, String range, LocalDate start, LocalDate end) {
//        Range r = normalizeRange(range, start, end);
//        switch (type.toLowerCase(Locale.ROOT)) {
//            case "revenue":
//                return buildRevenueChart(r);
//            case "employees":
//                return buildEmployeesPie(); // pie
//            case "customers":
//                return buildCustomersLine(r);
//            case "orders":
//                return buildOrdersPerDay(r);
//            default:
//                throw new IllegalArgumentException("Invalid chart type");
//        }
//    }
//
//    @Override
//    public List<TopProductResponse> getTopProducts(int limit, String range, LocalDate start, LocalDate end) {
//        Range r = normalizeRange(range, start, end);
//        return orderRepository.findTopProducts(r.start, r.end, limit);
//    }
//
//    @Override
//    public List<RecentOrderResponse> getRecentOrders(int limit) {
//        return orderRepository.findRecentOrders(limit);
//    }
//
//    // ================= helpers =================
//    private static class Range { LocalDate start, end; }
//    private Range normalizeRange(String range, LocalDate start, LocalDate end) {
//        Range r = new Range();
//        LocalDate today = LocalDate.now();
//        switch (range.toLowerCase(Locale.ROOT)) {
//            case "month": {
//                YearMonth ym = YearMonth.from(today);
//                r.start = ym.atDay(1);
//                r.end   = ym.atEndOfMonth();
//                break;
//            }
//            case "quarter": {
//                int q = (today.getMonthValue()-1)/3 + 1;
//                int startMonth = (q-1)*3 + 1;
//                r.start = LocalDate.of(today.getYear(), startMonth, 1);
//                r.end   = LocalDate.of(today.getYear(), startMonth+2, 1).withDayOfMonth(
//                        YearMonth.of(today.getYear(), startMonth+2).lengthOfMonth());
//                break;
//            }
//            case "year": {
//                r.start = LocalDate.of(today.getYear(), 1,1);
//                r.end   = LocalDate.of(today.getYear(), 12,31);
//                break;
//            }
//            case "custom": {
//                if (start == null || end == null) throw new IllegalArgumentException("Thiếu start/end cho custom");
//                r.start = start; r.end = end;
//                break;
//            }
//            default:
//                throw new IllegalArgumentException("range không hợp lệ");
//        }
//        return r;
//    }
//
//    private ChartDataResponse buildRevenueChart(Range r) {
//        var points = orderRepository.sumRevenueByDate(r.start, r.end, OrderStatus.HUY.name());
//        List<String> labels = new ArrayList<>();
//        List<Number> values = new ArrayList<>();
//        points.forEach(p -> { labels.add(p.getLabel()); values.add(p.getValue()); });
//
//        ChartDataResponse resp = new ChartDataResponse();
//        resp.setType("revenue");
//        resp.setLabels(labels);
//        resp.setValues(values);
//        return resp;
//    }
//
//    private ChartDataResponse buildEmployeesPie() {
//        long active   = employeeRepository.countByUserStatus(1);
//        long inactive = employeeRepository.countByUserStatus(0);
//        long blocked  = employeeRepository.countByUserStatus(-1);
//
//        ChartDataResponse resp = new ChartDataResponse();
//        resp.setType("employees");
//        resp.setLabels(List.of("Đang làm việc","Nghỉ việc","Bị khóa"));
//        resp.setValues(List.of(active, inactive, blocked));
//        return resp;
//    }
//
//    private ChartDataResponse buildCustomersLine(Range r) {
//        var points = customerRepository.countNewCustomersByDate(r.start, r.end);
//        List<String> labels = new ArrayList<>();
//        List<Number> values = new ArrayList<>();
//        points.forEach(p -> { labels.add(p.getLabel()); values.add(p.getValue()); });
//
//        ChartDataResponse resp = new ChartDataResponse();
//        resp.setType("customers");
//        resp.setLabels(labels);
//        resp.setValues(values);
//        return resp;
//    }
//
//    private ChartDataResponse buildOrdersPerDay(Range r) {
//        var points = orderRepository.countOrdersByDate(r.start, r.end);
//        List<String> labels = new ArrayList<>();
//        List<Number> values = new ArrayList<>();
//        points.forEach(p -> { labels.add(p.getLabel()); values.add(p.getValue()); });
//
//        ChartDataResponse resp = new ChartDataResponse();
//        resp.setType("orders");
//        resp.setLabels(labels);
//        resp.setValues(values);
//        return resp;
//    }
//}
