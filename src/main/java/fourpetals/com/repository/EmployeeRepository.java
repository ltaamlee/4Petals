// src/main/java/fourpetals/com/repository/EmployeeRepository.java
package fourpetals.com.repository;

import fourpetals.com.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    // Fetch luôn user + role để tránh N+1 khi map DTO từ user
    @Override
    @EntityGraph(attributePaths = {"user", "user.role"})
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
}
