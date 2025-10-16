// fourpetals/com/service/impl/CustomerServiceImpl.java
package fourpetals.com.service.impl;

import fourpetals.com.dto.response.customers.CustomerDetailResponse;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.Series;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.repository.UserRepository;
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

	private final CustomerRepository customerRepo;
	private final OrderRepository orderRepo;
	private final UserRepository userRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo, OrderRepository orderRepo, UserRepository userRepo) {
		super();
		this.customerRepo = customerRepo;
		this.orderRepo = orderRepo;
		this.userRepo = userRepo;
	}

	@Override
    public Page<CustomerRowVM> searchRows(String keyword, Gender gender, CustomerRank rank, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("maKH")));
        return customerRepo.searchRows(keyword == null ? "" : keyword.trim(), gender, rank, pageable);
    }

    @Override
    public Optional<CustomerDetailResponse> getDetail(Integer maKH) {
        return customerRepo.getDetail(maKH);
    }

    @Override
    public long countAll() { return customerRepo.count(); }

    @Override
    public long countNewInCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = start.plusMonths(1);
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT   = end.atStartOfDay();
        return customerRepo.countNewBetween(startDT, endDT);
    }

    @Override
    public long countByGender(Gender gender) { return customerRepo.countByGioiTinh(gender); }

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

        var rows = customerRepo.countNewByDayBetween(startDT, endDT);

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
        Customer c = customerRepo.findById(maKH).orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        c.setHangThanhVien(rank);
        customerRepo.save(c);
    }

	@Override
	public long countByGioiTinh(Gender gioiTinh) {
		return customerRepo.countByGioiTinh(gioiTinh);
	}

	@Override
	public long countByHangThanhVien(CustomerRank hangThanhVien) {
	    return customerRepo.countByHangThanhVien(hangThanhVien);
	}

	@Override
	public Customer updateCustomer(Customer customer) {
	    return customerRepo.save(customer);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Customer> findById(Integer maKH) {
		return customerRepo.findById(maKH);
	}

	@Override
	public Optional<Customer> findByUser(User username) {
		return customerRepo.findByUser(username);
	}

	@Override
	public Optional<Customer> findBySdt(String sdt) {
		return customerRepo.findBySdt(sdt);
	}

	@Override
	public List<Customer> findAll() {
		return customerRepo.findAll();
	}

	@Override
	public Page<Customer> searchCustomers(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer registerCustomer(Customer customer, String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void linkUserWithCustomer(User user, Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerRank getCustomerRank(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAvatar(String username, String imageUrl) {
		userRepo.findByUsername(username).ifPresent(user -> {
			user.setImageUrl(imageUrl);
			userRepo.save(user);
		});
		
	}

	@Override
	public Optional<Customer> findByUsername(String username) {
	    return customerRepo.findByUser_Username(username);
	}

	@Override
	public Customer save(Customer customer) {
		return customerRepo.save(customer);
	}
}