// src/main/java/fourpetals/com/service/impl/EmployeeServiceImpl.java
package fourpetals.com.service.impl;

import fourpetals.com.dto.request.users.EmployeeRequest;
import fourpetals.com.dto.request.users.UserRequest;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.Gender;
import fourpetals.com.enums.RoleName;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.mapper.UserMapping;

import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.repository.RoleRepository;
import fourpetals.com.service.EmployeeService;
import fourpetals.com.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.EnumSet;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final UserService userService;
	private final RoleRepository roleRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService,
			RoleRepository roleRepository) {
		super();
		this.employeeRepository = employeeRepository;
		this.userService = userService;
		this.roleRepository = roleRepository;
	}

	@Override
	@Transactional
	public UserDetailResponse createEmployee(EmployeeRequest request) {
		Role role = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new RuntimeException("Role không tồn tại"));

		// Tạo User
		UserRequest uReq = request.getUser();
		User user = new User();
		user.setUsername(uReq.getUsername());
		user.setEmail(uReq.getEmail());
		user.setPassword(uReq.getPassword());
		user.setRole(role);

		// Lưu User
		user = userService.registerUser(user, role.getRoleName().name());

		// Tạo Employee liên kết với User
		Employee emp = new Employee();
		emp.setHoTen(request.getHoTen());
		emp.setNgaySinh(request.getNgaySinh());
		emp.setGioiTinh(request.getGioiTinh());
		emp.setSdt(request.getSdt());
		emp.setUser(user);

		// Lưu Employee
		employeeRepository.save(emp);

		// Liên kết lại User với Employee
		user.setNhanVien(emp);
		userService.updateUser(user);

		// Trả về DTO chi tiết User (có đầy đủ ngày sinh, giới tính)
		return UserDetailResponse.fromUser(user);
	}

	@Override
	@Transactional
	public UserDetailResponse updateEmployee(UserDetailResponse request) {
		Integer userId = request.getUserId();
		User user = userService.findById(userId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

		Employee employee = employeeRepository.findByUser(user).orElseThrow(
				() -> new RuntimeException("Không tìm thấy nhân viên tương ứng với người dùng ID: " + userId));

		// Cập nhật Role nếu có
		if (request.getRoleName() != null) {
			try {
				RoleName roleEnum = RoleName.valueOf(request.getRoleName().toUpperCase());
				Role role = roleRepository.findByRoleName(roleEnum)
						.orElseThrow(() -> new RuntimeException("Không tìm thấy Role: " + roleEnum));
				user.setRole(role);
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException("Giá trị RoleName không hợp lệ: " + request.getRoleName());
			}
		}

		// Cập nhật thông tin User
		if (request.getUsername() != null && !request.getUsername().isEmpty()) {
			user.setUsername(request.getUsername());
		}
		if (request.getEmail() != null && !request.getEmail().isEmpty()) {
			user.setEmail(request.getEmail());
		}

		// Cập nhật thông tin Employee
		if (request.getPhone() != null && !request.getPhone().isEmpty()) {
			employee.setSdt(request.getPhone());
		}
		if (request.getFullName() != null && !request.getFullName().isEmpty()) {
			employee.setHoTen(request.getFullName());
		}
		if (request.getBirthDate() != null) {
			employee.setNgaySinh(request.getBirthDate());
		}
		if (request.getGender() != null && !request.getGender().isEmpty()) {
			try {
				Gender genderEnum = Gender.valueOf(request.getGender().toUpperCase());
				employee.setGioiTinh(genderEnum);
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException("Giá trị giới tính không hợp lệ: " + request.getGender());
			}
		}

		employeeRepository.save(employee);
		user.setNhanVien(employee);
		userService.updateUser(user);

		return UserMapping.toUserResponse(user);
	}

	@Override
	@Transactional
	public void deleteUser(Integer userId) {
		// Tìm user theo ID
		User user = userService.findById(userId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

		// Xóa Employee nếu có
		userService.findEmployeeByUser(user).ifPresent(emp -> {
			emp.setUser(null); // Bỏ liên kết với User
			user.setNhanVien(null);
			employeeRepository.delete(emp);
		});
//
//	    // Xóa Customer nếu có
//	    userService.findCustomerByUser(user).ifPresent(cus -> {
//	        cus.setUser(null);          // Bỏ liên kết với User
//	        user.setKhachHang(null);
//	        customerRepository.delete(cus);
//	    });

		// Xóa User
		userService.deleteUser(user);
	}

	private static Specification<Employee> keywordLike(String keyword) {
		return (root, q, cb) -> {
			if (keyword == null || keyword.isBlank())
				return cb.conjunction();
			String like = "%" + keyword.trim().toLowerCase() + "%";
			Join<Employee, User> u = root.join("user", JoinType.INNER);
			return cb.or(cb.like(cb.lower(root.get("hoTen")), like), cb.like(cb.lower(u.get("username")), like),
					cb.like(cb.lower(u.get("email")), like));
		};
	}

	private static Specification<Employee> userStatusIs(Integer status) {
		return (root, q, cb) -> {
			if (status == null)
				return cb.conjunction();
			Join<Employee, User> u = root.join("user", JoinType.INNER);
			return cb.equal(u.get("status"), status); // 1/0/-1
		};
	}
	// =========================================================

	@SuppressWarnings("removal")
	private Specification<Employee> baseSpec(String keyword) {
	    return Specification.where(keywordLike(keyword));
	}

	@Override
	public Page<UserDetailResponse> search(String keyword, String status, Pageable pageable) {
	    Integer st = (status == null || status.isBlank()) ? null : Integer.valueOf(status);
	    Specification<Employee> spec = baseSpec(keyword).and(userStatusIs(st));

	    Page<Employee> page = employeeRepository.findAll(spec, pageable);
	    return page.map(e -> UserMapping.toUserResponse(e.getUser()));
	}
//
//	@Override
//    public UserStatsResponse stats() {
//        long total = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS));
//        long active = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.ACTIVE.getValue())));
//        long inactive = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.INACTIVE.getValue())));
//        long blocked = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.BLOCKED.getValue())));
//        return new UserStatsResponse(total, active, inactive, blocked);
//    }
}
