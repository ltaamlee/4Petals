// src/main/java/fourpetals/com/service/EmployeeService.java
package fourpetals.com.service;

import fourpetals.com.dto.request.users.EmployeeRequest;
import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

	// CRUD
	UserDetailResponse createEmployee(EmployeeRequest request);

	UserDetailResponse updateEmployee(UserDetailResponse request);

	void deleteUser(Integer userId);
//
	Page<UserDetailResponse> search(String keyword, String status, Pageable pageable);
//
//	UserStatsResponse stats();

}
