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
//@RestController
//@RequestMapping("/admin/users")
//public class AdminPermissionController {
//
//    @Autowired
//    private PermissionService permissionService;
//
//    @GetMapping("/{userId}/permissions")
//    public List<PermissionDto> getUserPermissions(@PathVariable Integer userId) {
//        return permissionService.getPermissionsForUser(userId);
//    }
//
//    @PostMapping("/permissions/update")
//    public String updatePermissions(@RequestParam Integer userId,
//                                    @RequestParam List<Integer> permissionIds) {
//        permissionService.updateUserPermissions(userId, permissionIds);
//        return "redirect:/admin/permissions"; 
//    }
//}
