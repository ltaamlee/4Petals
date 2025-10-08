package fourpetals.com.controller.admin.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer roleId,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> userPage;
               
        model.addAttribute("activeUsers", userService.countByStatus(UserStatus.ACTIVE));
        model.addAttribute("inactiveUsers", userService.countByStatus(UserStatus.INACTIVE));
        model.addAttribute("blockedUsers", userService.countByStatus(UserStatus.BLOCKED));
               
        model.addAttribute("contentFragment", "admin/users/list :: users");
        model.addAttribute("pageTitle", "Quản lý người dùng");
        return "admin/users/list";
    }
    
    
}