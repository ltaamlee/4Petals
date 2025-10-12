package fourpetals.com.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/supplier")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSupplierController {

}
