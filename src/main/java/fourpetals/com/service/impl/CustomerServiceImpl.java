
package fourpetals.com.service.impl;

import fourpetals.com.entity.Customer;
import fourpetals.com.model.CustomerRank;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.CustomerStatsVM;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo, OrderRepository orderRepo) {
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public List<CustomerRowVM> list(CustomerRank filterRank, Month filterMonth, Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        List<Customer> all = customerRepo.findAll();
        List<CustomerRowVM> rows = new ArrayList<>(all.size());

        for (Customer c : all) {
            String email = (c.getUser() != null && c.getUser().getEmail() != null)
                    ? c.getUser().getEmail() : null;

            long total = orderRepo.countByKhachHang_MaKH(c.getMaKH());

            LocalDateTime firstDT = orderRepo.firstOrderDateTimeOfCustomer(c.getMaKH());
            LocalDate firstDate = (firstDT != null) ? firstDT.toLocalDate() : null;

            CustomerRowVM row = CustomerRowVM.from(c, email, total, firstDate);

            if (filterRank != null && row.hang() != filterRank) continue;

            if (filterMonth != null) {
                if (firstDate == null) continue;
                if (firstDate.getYear() != y || firstDate.getMonthValue() != filterMonth.getValue()) continue;
            }

            rows.add(row);
        }

        rows.sort(Comparator.comparing(CustomerRowVM::ngayTao,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return rows;
    }

    @Override
    public CustomerStatsVM stats(Integer year, Month filterMonth) {
        int y = (year != null) ? year : LocalDate.now().getYear();

        long totalCustomers = customerRepo.count();

        // Tính số KH mới theo tháng (1..12)
        Map<Integer, Long> byMonth = new HashMap<>();
        for (Object[] row : orderRepo.firstOrderMonthPerCustomer(y)) {
            // row là (tháng của đơn đầu tiên theo từng KH, số KH (nhưng đang đếm theo từng KH))
            // Query trên trả mỗi KH một hàng; ta gộp lại theo tháng:
            Integer m = ((Number) row[0]).intValue();
            byMonth.merge(m, 1L, Long::sum);
        }
        for (int m = 1; m <= 12; m++) byMonth.putIfAbsent(m, 0L);

        long newInMonth;
        if (filterMonth != null) {
            newInMonth = byMonth.getOrDefault(filterMonth.getValue(), 0L);
        } else {
            int mNow = LocalDate.now().getMonthValue();
            newInMonth = byMonth.getOrDefault(mNow, 0L);
        }

        return new CustomerStatsVM(totalCustomers, newInMonth, byMonth);
    }

    @Override public Optional<Customer> findById(Integer id){ return customerRepo.findById(id); }
    @Override public Customer save(Customer c){ return customerRepo.save(c); }
    @Override public void deleteById(Integer id){ customerRepo.deleteById(id); }
}
