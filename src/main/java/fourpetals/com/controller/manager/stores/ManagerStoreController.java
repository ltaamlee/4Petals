package fourpetals.com.controller.manager.stores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerStoreController {
	@GetMapping("/stores")
	public String stores(Model model) {
		model.addAttribute("pageTitle", "Quản lý cửa hàng");
		return "manager/stores/list";
	}
}
