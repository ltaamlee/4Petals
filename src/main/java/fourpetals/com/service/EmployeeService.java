// src/main/java/fourpetals/com/service/EmployeeService.java
package fourpetals.com.service;

import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<UserDetailResponse> search(String keyword, String status, Pageable pageable);
    UserStatsResponse stats();
}
