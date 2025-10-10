//package fourpetals.com.controller.admin;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import fourpetals.com.entity.Role;
//import fourpetals.com.entity.User;
//import fourpetals.com.security.CustomUserDetails;
//import fourpetals.com.service.RoleService;
//import fourpetals.com.service.UserService;
//
//@Controller
//@RequestMapping("/admin/permissions")
//public class AdminPermissionsController {
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private RoleService roleService;
//
//	@GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
//
//	public String listUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword,
//			@RequestParam(required = false) String status, @RequestParam(required = false) Integer roleId,
//			@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//
//		if (userDetails != null) {
//			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
//			userOpt.ifPresent(u -> model.addAttribute("user", u));
//		}
//
//		Pageable pageable = PageRequest.of(page, 10);
//
//		List<Role> roles = roleService.findAll();
//		model.addAttribute("roles", roles);
//
//		Page<User> userPage = userService.searchUsers(keyword, status, roleId, pageable);
//
//		model.addAttribute("users", userPage.getContent());
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPages", userPage.getTotalPages());
//		model.addAttribute("totalElements", userPage.getTotalElements());
//		model.addAttribute("keyword", keyword);
//		model.addAttribute("status", status);
//		model.addAttribute("roleId", roleId);
//
//		return "admin/permissions/list";
//	}
//}
