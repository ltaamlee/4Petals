package fourpetals.com.controller.auth;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.User;
import fourpetals.com.repository.UserRepository;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Bộ nhớ tạm lưu OTP (thay cho database)
    private ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    private String currentEmail = null;

    /* ======= BƯỚC 1: NHẬP EMAIL ĐỂ NHẬN OTP ======= */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password"; // form nhập email
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {

        // Kiểm tra email có trong hệ thống không
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
            return "auth/forgot-password";
        }

        // Nếu có, lấy user ra
        User user = optionalUser.get();

        // Sinh mã OTP ngẫu nhiên 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Lưu OTP vào bộ nhớ tạm
        otpStorage.put(email, otp);
        currentEmail = email;

        // Gửi OTP qua email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã xác nhận đặt lại mật khẩu (4Petals)");
        message.setText("Xin chào " + user.getUsername() + ",\n\nMã OTP của bạn là: " + otp + 
                        "\nVui lòng không chia sẻ mã này cho bất kỳ ai.\n\nTrân trọng,\nĐội ngũ 4Petals.");
        mailSender.send(message);

        model.addAttribute("email", email);
        model.addAttribute("success", "Mã OTP đã được gửi đến email của bạn!");
        return "auth/otp"; // form nhập OTP
    }


    /* ======= BƯỚC 2: NHẬP MÃ OTP ======= */
    @GetMapping("/forgot-password/verify-otp")
    public String showVerifyOtpForm() {
        return "auth/otp";
    }

    @PostMapping("/forgot-password/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp, Model model) {
        if (currentEmail == null || !otpStorage.containsKey(currentEmail)) {
            model.addAttribute("error", "Vui lòng nhập lại email để nhận mã OTP mới!");
            return "auth/forgot-password";
        }

        String storedOtp = otpStorage.get(currentEmail);
        if (storedOtp.equals(otp)) {
            model.addAttribute("success", "Xác minh OTP thành công. Hãy đặt lại mật khẩu mới!");
            return "auth/reset-password"; // form đặt lại mật khẩu
        } else {
            model.addAttribute("error", "Mã OTP không đúng!");
            return "auth/otp";
        }
    }

    /* ======= BƯỚC 3: ĐẶT LẠI MẬT KHẨU ======= */
    @GetMapping("/forgot-password/reset-password")
    public String showResetPasswordForm() {
        return "auth/reset-password";
    }

    @PostMapping("/forgot-password/reset-password")
    public String resetPassword(@RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        if (currentEmail == null) {
            model.addAttribute("error", "Phiên làm việc không hợp lệ!");
            return "auth/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "auth/reset-password";
        }

        // Lấy user theo email
        Optional<User> optionalUser = userRepository.findByEmail(currentEmail);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy người dùng!");
            return "auth/forgot-password";
        }

        User user = optionalUser.get();
        user.setPassword(newPassword);
        userRepository.save(user);

        // Xóa thông tin OTP
        otpStorage.remove(currentEmail);
        currentEmail = null;

        model.addAttribute("success", "Đặt lại mật khẩu thành công! Hãy đăng nhập lại.");
        return "auth/login";
    }
}
