package fourpetals.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.RoleName;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.repository.RoleRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// -----------------------
	// Tìm kiếm
	// -----------------------
	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findByEmailAndStatus(String email, UserStatus status) {
		return userRepository.findByEmailAndStatus(email, status.getValue());
	}

	@Override
	public Optional<User> findByUsernameAndStatus(String username, UserStatus status) {
		return userRepository.findByUsernameAndStatus(username, status.getValue());
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public List<User> findByStatus(UserStatus status) {
		return userRepository.findByStatus(status.getValue());
	}

	@Override
	public List<User> findByRole(Role role) {
		return userRepository.findByRole(role);
	}

	@Override
	public List<User> findByRoleAndStatus(Role role, UserStatus status) {
		return userRepository.findByRoleAndStatus(role, status.getValue());
	}

	// -----------------------
	// Kiểm tra tồn tại
	// -----------------------
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmailAndStatus(String email, UserStatus status) {
		return userRepository.existsByEmailAndStatus(email, status.getValue());
	}

	@Override
	public boolean existsByUsernameAndStatus(String username, UserStatus status) {
		return userRepository.existsByUsernameAndStatus(username, status.getValue());
	}

	// -----------------------
	// Đếm
	// -----------------------
	@Override
	public long countByStatus(UserStatus status) {
		return userRepository.countByStatus(status.getValue());
	}

	@Override
	public long countByRole(Role role) {
		return userRepository.countByRole(role);
	}

	@Override
	public long countByRoleAndStatus(Role role, UserStatus status) {
		return userRepository.countByRoleAndStatus(role, status.getValue());
	}

	// -----------------------
	// Đăng ký User mới
	// -----------------------
	@Override
	public User registerUser(User user, String roleName) {

		if (existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email đã tồn tại");
		}

		if (existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username đã tồn tại");
		}

		// Hash password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Gán role
		Role role = roleRepository.findByRoleName(RoleName.valueOf(roleName))
				.orElseThrow(() -> new RuntimeException("Role không tồn tại"));
		user.setRole(role);

		// Trạng thái ACTIVE mặc định
		user.setStatus(UserStatus.ACTIVE.getValue());

		// Avatar mặc định nếu cần
		if (user.getImageUrl() == null || user.getImageUrl().isEmpty()) {
			user.setImageUrl("profile/customer/default.png");
		}

		return userRepository.save(user);
	}

	// -----------------------
	// Đăng nhập
	// -----------------------
	@Override
	public Optional<User> login(String usernameOrEmail, String password) {
		// Tìm user theo username hoặc email
		Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
		if (userOpt.isEmpty()) {
			userOpt = userRepository.findByEmail(usernameOrEmail);
		}

		if (userOpt.isEmpty()) {
			return Optional.empty(); // user không tồn tại
		}

		User user = userOpt.get();
		System.out.println("Login attempt: " + usernameOrEmail);
		System.out.println("User found: " + userOpt.isPresent());
		if (userOpt.isPresent()) {
			User u = userOpt.get();
			System.out.println("Password matches: " + passwordEncoder.matches(password, u.getPassword()));
			System.out.println("User status: " + u.getStatus());
			System.out.println("Role: " + u.getRole().getRoleName());
		}

		// Kiểm tra trạng thái
		if (!UserStatus.fromValue(user.getStatus()).canLogin()) {
			return Optional.empty(); // user không được phép login
		}

		// Kiểm tra mật khẩu
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return Optional.empty(); // mật khẩu sai
		}

		return Optional.of(user); // đăng nhập thành công
	}

	// -----------------------
	// Cập nhật User
	// -----------------------
	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	// -----------------------
	// Đổi mật khẩu
	// -----------------------
	@Override
	public void changePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	// -----------------------
	// Thay đổi trạng thái
	// -----------------------
	@Override
	public void setStatus(User user, UserStatus status) {
		user.setStatus(status.getValue());
		userRepository.save(user);
	}

	// -----------------------
	// Xóa User
	// -----------------------
	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	// -----------------------
	// Tìm kiếm nâng cao
	// -----------------------
	@Override
	public List<User> searchByUsername(String keyword) {
		return userRepository.findByUsernameContainingIgnoreCase(keyword);
	}

	@Override
	public List<User> findTop10ByStatusOrderByCreatedAtDesc(UserStatus status) {
		return userRepository.findTop10ByStatusOrderByCreatedAtDesc(status.getValue());
	}
}
