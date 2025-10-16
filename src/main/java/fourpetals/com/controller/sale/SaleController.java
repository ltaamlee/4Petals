package fourpetals.com.controller.sale;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fourpetals.com.entity.User;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.RoleService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/sale")
@PreAuthorize("hasRole('SALES_EMPLOYEE')")
public class SaleController {

    private final UserService userService;
    private final RoleService roleService;
    
	public SaleController(UserService userService, RoleService roleService) {
		super();
		this.userService = userService;
		this.roleService = roleService;
	}

	// Đơn hàng
	@GetMapping("/orders")
	public String order(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
	        System.out.println("Username: " + userDetails.getUsername());
	        System.out.println("Authorities: " + userDetails.getAuthorities());
	        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
	        userOpt.ifPresent(user -> model.addAttribute("user", user));
	    } else {
	        System.out.println("userDetails is null");
	    }

		return "sale/orders";
	}
	
	
	//Sản phẩm - Xem - Lọc - Tìm kiếm - Sắp xếp
	@GetMapping("/products")
	public String product(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
	        System.out.println("Username: " + userDetails.getUsername());
	        System.out.println("Authorities: " + userDetails.getAuthorities());
	        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
	        userOpt.ifPresent(user -> model.addAttribute("user", user));
	    } else {
	        System.out.println("userDetails is null");
	    }

		return "sale/products";
	}
	
	//Profile
	@GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            if (userOpt.isPresent()) {
                model.addAttribute("user", userOpt.get());
            } else {
                model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
                return "sale/error";
            }
        }
        return "sale/profile";
    }

}
