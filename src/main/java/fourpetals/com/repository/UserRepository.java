package fourpetals.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // -----------------------
    // Tìm kiếm User
    // -----------------------
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndStatus(String email, Integer status);
    Optional<User> findByUsernameAndStatus(String username, Integer status);

    List<User> findByStatus(Integer status);
    List<User> findByRole(Role role);
    List<User> findByRoleAndStatus(Role role, Integer status);
   

    // -----------------------
    // Kiểm tra tồn tại
    // -----------------------
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmailAndStatus(String email, Integer status);
    boolean existsByUsernameAndStatus(String username, Integer status);

    // -----------------------
    // Đếm số lượng
    // -----------------------
    long countByStatus(Integer status);
    long countByRole(Role role);
    long countByRoleAndStatus(Role role, Integer status);
    // -----------------------
    // Xóa
    // -----------------------
    void deleteByEmail(String email);
    void deleteByUsername(String username);

    // -----------------------
    // Optional nâng cao
    // -----------------------
    List<User> findTop10ByStatusOrderByCreatedAtDesc(Integer status); // 10 user mới nhất theo status
    List<User> findByUsernameContainingIgnoreCase(String keyword);   // search username
}
