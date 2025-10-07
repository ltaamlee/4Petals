package fourpetals.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Role;
import fourpetals.com.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Tìm role theo tên RoleName (enum)
    Optional<Role> findByRoleName(RoleName roleName);
    
    // Tùy chọn: kiểm tra tồn tại theo RoleName
    boolean existsByRoleName(RoleName roleName);

}
