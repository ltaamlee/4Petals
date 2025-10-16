package fourpetals.com.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import fourpetals.com.entity.Employee;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.RoleName;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.repository.EmployeeRepository;
import fourpetals.com.repository.RoleRepository;
import fourpetals.com.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final EmployeeRepository employeeRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
			EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.employeeRepository = employeeRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// Kiểm tra admin đã tồn tại chưa
			if (userRepository.findByUsername("admin").isPresent()) {
				System.out.println("✓ Tài khoản admin đã tồn tại!");
				return;
			}

			// Tạo Role ADMIN nếu chưa tồn tại
			Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN).orElseGet(() -> {
				Role r = new Role();
				r.setRoleName(RoleName.ADMIN);
				Role savedRole = roleRepository.save(r);
				System.out.println("✓ Role ADMIN được tạo thành công!");
				return savedRole;
			});

			// Tạo User
			User admin = new User();
			admin.setUsername("admin");
			admin.setEmail("admin@4petals.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setStatus(UserStatus.ACTIVE.getValue());
			admin.setRole(adminRole);

			// Lưu User
			User savedUser = userRepository.saveAndFlush(admin);
			System.out.println("✓ User admin được tạo thành công!");

			// Tạo Employee
			Employee emp = new Employee();
			emp.setHoTen("Quản Trị Viên");
			emp.setSdt("0123456789");
			emp.setUser(savedUser); 

			// Lưu Employee
			Employee savedEmp = employeeRepository.saveAndFlush(emp);
			System.out.println("✓ Employee được tạo thành công!");

			// Cập nhật User với Employee
			savedUser.setNhanVien(savedEmp);
			userRepository.saveAndFlush(savedUser);

			System.out.println("════════════════════════════════════");
			System.out.println("✓ Tài khoản admin được tạo thành công!");
			System.out.println("  Username: admin");
			System.out.println("  Password: admin123");
			System.out.println("  Email: admin@4petals.com");
			System.out.println("════════════════════════════════════");

		} catch (Exception e) {
			System.out.println("✗ Lỗi khi tạo tài khoản admin!");
			System.out.println("  Lỗi: " + e.getMessage());
			System.out.println("  Nguyên nhân: " + e.getCause());
			e.printStackTrace();
		}
       
        
        Role managerRole = roleRepository.findByRoleName(RoleName.MANAGER)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(RoleName.MANAGER);
                    return roleRepository.save(r);
                });

        
        final String managerUsername = "manager";
        if (!userRepository.existsByUsername(managerUsername)) {
            User manager = new User();
            manager.setUsername(managerUsername);
            manager.setEmail("manager@4petals.com");
            manager.setPassword(passwordEncoder.encode("123"));
            manager.setRole(managerRole);
            manager.setStatus(UserStatus.ACTIVE.getValue());
            manager.setImageUrl("profile/manager/default.png");
            User savedManager = userRepository.save(manager);

            // ---- Tạo Employee cho manager ----
            Employee managerEmp = new Employee();
            managerEmp.setHoTen("Quản Lý Cửa Hàng");
            managerEmp.setSdt("0987654321"); // Số điện thoại ví dụ
            managerEmp.setUser(savedManager);

            Employee savedManagerEmp = employeeRepository.save(managerEmp);

            // Cập nhật User với Employee
            savedManager.setNhanVien(savedManagerEmp);
            userRepository.save(savedManager);

            System.out.println("✓ Tài khoản manager và Employee được tạo thành công!");
        }
        
        
    }
}
