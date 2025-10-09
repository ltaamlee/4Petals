package fourpetals.com.controller.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Role;
import fourpetals.com.entity.User;
import fourpetals.com.enums.RoleName;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.repository.CustomerRepository;
import fourpetals.com.repository.RoleRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public RegisterController(UserRepository userRepository, RoleRepository roleRepository,
                              CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder,
                              JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        User user = new User();
        user.setKhachHang(new Customer());
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model, HttpServletResponse response) {

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "auth/register";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role customerRole = roleRepository.findByRoleName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER không tồn tại"));
        user.setRole(customerRole);

        user.setStatus(UserStatus.ACTIVE.getValue());

        user.setImageUrl("profile/customer/default.png");

        User savedUser = userRepository.save(user);


        Customer customerForm = user.getKhachHang();
        customerForm.setUser(savedUser);
        Customer savedCustomer = customerRepository.save(customerForm);
        savedUser.setKhachHang(savedCustomer);
        userRepository.save(savedUser);


        String token = tokenProvider.generateToken(savedUser.getUsername());
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); 
        response.addCookie(cookie);

        model.addAttribute("success", "Đăng ký thành công!");
        model.addAttribute("user", new User());
        return "auth/register";
    }
}