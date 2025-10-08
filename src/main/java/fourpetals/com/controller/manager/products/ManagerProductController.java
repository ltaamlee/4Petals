package fourpetals.com.controller.manager.products;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerProductController {
	@GetMapping("/products")
	public String products(Model model) {
		model.addAttribute("pageTitle", "Quản lý Sản phẩm");
		return "manager/products";
	}
}
