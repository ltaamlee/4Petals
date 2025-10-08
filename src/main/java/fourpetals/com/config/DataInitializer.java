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

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(RoleName.ADMIN);
                    return roleRepository.save(r);
                });

        String adminUsername = "admin";
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@4petals.com");
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRole(adminRole);
            admin.setStatus(UserStatus.ACTIVE.getValue());
            admin.setImageUrl("profile/admin/default.png");
            userRepository.save(admin);
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
            userRepository.save(manager);
        }
        
        
    }
}
