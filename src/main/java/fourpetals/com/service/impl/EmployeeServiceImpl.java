// src/main/java/fourpetals/com/service/impl/EmployeeServiceImpl.java
package fourpetals.com.service.impl;

import fourpetals.com.dto.response.stats.UserStatsResponse;
import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.EmployeePosition;   // enum vị trí NV của cậu
import fourpetals.com.enums.UserStatus;
import fourpetals.com.mapper.UserMapping;       // đã có sẵn, map User -> UserDetailResponse
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.service.EmployeeService;
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

    @Autowired
    private EmployeeRepository employeeRepository;

    // Chỉ hiển thị các vị trí vận hành — ẨN ADMIN/MANAGER
    private static final Set<EmployeePosition> ALLOWED_POSITIONS = EnumSet.of(
            EmployeePosition.SALES_EMPLOYEE,
            EmployeePosition.INVENTORY_EMPLOYEE,
            EmployeePosition.SHIPPER
    );

    // ===== Inline Specifications (không tạo package mới) =====
    private static Specification<Employee> positionsAllowed(Set<EmployeePosition> allowed) {
        return (root, q, cb) -> {
            root.join("user", JoinType.INNER); // đảm bảo có liên kết User
            if (allowed == null || allowed.isEmpty()) return cb.conjunction();
            return root.get("chucVu").in(allowed); // Employee.chucVu là EmployeePosition
        };
    }

    private static Specification<Employee> keywordLike(String keyword) {
        return (root, q, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            String like = "%" + keyword.trim().toLowerCase() + "%";
            Join<Employee, User> u = root.join("user", JoinType.INNER);
            return cb.or(
                cb.like(cb.lower(root.get("hoTen")), like),
                cb.like(cb.lower(u.get("username")), like),
                cb.like(cb.lower(u.get("email")), like)
            );
        };
    }

    private static Specification<Employee> userStatusIs(Integer status) {
        return (root, q, cb) -> {
            if (status == null) return cb.conjunction();
            Join<Employee, User> u = root.join("user", JoinType.INNER);
            return cb.equal(u.get("status"), status); // 1/0/-1
        };
    }
    // =========================================================

    private Specification<Employee> baseSpec(String keyword) {
        return Specification.where(positionsAllowed(ALLOWED_POSITIONS))
                            .and(keywordLike(keyword));
    }

    @Override
    public Page<UserDetailResponse> search(String keyword, String status, Pageable pageable) {
        Integer st = (status == null || status.isBlank()) ? null : Integer.valueOf(status);
        Specification<Employee> spec = baseSpec(keyword).and(userStatusIs(st));

        Page<Employee> page = employeeRepository.findAll(spec, pageable);
        return page.map(e -> UserMapping.toUserResponse(e.getUser()));
    }

    @Override
    public UserStatsResponse stats() {
        long total = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS));
        long active = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.ACTIVE.getValue())));
        long inactive = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.INACTIVE.getValue())));
        long blocked = employeeRepository.count(positionsAllowed(ALLOWED_POSITIONS).and(userStatusIs(UserStatus.BLOCKED.getValue())));
        return new UserStatsResponse(total, active, inactive, blocked);
    }
}
