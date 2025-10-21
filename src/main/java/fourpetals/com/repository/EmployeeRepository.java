// src/main/java/fourpetals/com/repository/EmployeeRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.User;
import fourpetals.com.enums.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.domain.Specification;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

	List<Employee> findByUserRoleRoleNameOrderByHoTenAsc(RoleName roleName);

	// ===== TÌM KIẾM =====
	Optional<Employee> findById(Integer maNV);

	Optional<Employee> findByUser(User user);

	@Override
	@EntityGraph(attributePaths = { "user", "user.role" })
	Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
}
