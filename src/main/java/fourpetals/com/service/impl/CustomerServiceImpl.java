// fourpetals/com/service/impl/CustomerServiceImpl.java
package fourpetals.com.service.impl;

import fourpetals.com.dto.response.customers.CustomerDetailResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.Series;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired private CustomerRepository repo;

    @Override
    public Page<CustomerRowVM> searchRows(String keyword, Gender gender, CustomerRank rank, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("maKH")));
        return repo.searchRows(keyword == null ? "" : keyword.trim(), gender, rank, pageable);
    }

    @Override
    public Optional<CustomerDetailResponse> getDetail(Integer maKH) {
        return repo.getDetail(maKH);
    }

    @Override
    public long countAll() { return repo.count(); }

    @Override
    public long countNewInCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = start.plusMonths(1);
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT   = end.atStartOfDay();
        return repo.countNewBetween(startDT, endDT);
    }

    @Override
    public long countByGender(Gender gender) { return repo.countByGioiTinh(gender); }

    @Override
    public Series getNewCustomersSeries(String range, LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (start == null || end == null) {
            switch (range == null ? "month" : range) {
                case "quarter" -> {
                    int q = (today.getMonthValue() - 1) / 3;
                    start = LocalDate.of(today.getYear(), q * 3 + 1, 1);
                    end = start.plusMonths(3);
                }
                case "year" -> {
                    start = LocalDate.of(today.getYear(), 1, 1);
                    end = start.plusYears(1);
                }
                default -> {
                    start = today.with(TemporalAdjusters.firstDayOfMonth());
                    end = start.plusMonths(1);
                }
            }
        }
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT   = end.atStartOfDay();

        var rows = repo.countNewByDayBetween(startDT, endDT);

        Map<LocalDate, Integer> byDay = new HashMap<>();
        for (Object[] r : rows) {
            LocalDate d;
            Object d0 = r[0];
            if (d0 instanceof LocalDate ld) d = ld;
            else if (d0 instanceof java.sql.Date sd) d = sd.toLocalDate();
            else d = LocalDate.parse(d0.toString());
            int cnt = ((Number) r[1]).intValue();
            byDay.put(d, cnt);
        }

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        switch (range) {
            case "year" -> {
                for (int m = 1; m <= 12; m++) {
                    LocalDate mStart = LocalDate.of(start.getYear(), m, 1);
                    LocalDate mEnd   = mStart.plusMonths(1);
                    labels.add("Thg " + m);
                    values.add(sumRange(byDay, mStart, mEnd));
                }
            }
            case "quarter" -> {
                for (int i = 0; i < 3; i++) {
                    LocalDate mStart = start.plusMonths(i);
                    LocalDate mEnd   = mStart.plusMonths(1);
                    labels.add("Thg " + mStart.getMonthValue());
                    values.add(sumRange(byDay, mStart, mEnd));
                }
            }
            default -> {
                LocalDate d = start;
                while (d.isBefore(end)) {
                    labels.add(String.valueOf(d.getDayOfMonth()));
                    values.add(byDay.getOrDefault(d, 0));
                    d = d.plusDays(1);
                }
            }
        }
        return new Series(labels, values);
    }

    private int sumRange(Map<LocalDate, Integer> map, LocalDate from, LocalDate toExcl) {
        int sum = 0;
        for (LocalDate d = from; d.isBefore(toExcl); d = d.plusDays(1)) {
            sum += map.getOrDefault(d, 0);
        }
        return sum;
    }

    @Override
    @Transactional
    public void updateRank(Integer maKH, CustomerRank rank) {
        Customer c = repo.findById(maKH).orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        c.setHangThanhVien(rank);
        repo.save(c);
    }
}