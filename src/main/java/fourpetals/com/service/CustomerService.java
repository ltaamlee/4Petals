// fourpetals/com/service/CustomerService.java
package fourpetals.com.service;

import fourpetals.com.dto.response.customers.CustomerDetailResponse;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.Gender;
import fourpetals.com.model.CustomerRowVM;
import fourpetals.com.model.Series;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomerService {
    Page<CustomerRowVM> searchRows(String keyword, Gender gender, CustomerRank rank, int page, int size);
    Optional<CustomerDetailResponse> getDetail(Integer maKH);

    long countAll();
    long countNewInCurrentMonth();
    long countByGender(Gender gender);

    Series getNewCustomersSeries(String range, LocalDate start, LocalDate end);

    void updateRank(Integer maKH, CustomerRank rank);
}