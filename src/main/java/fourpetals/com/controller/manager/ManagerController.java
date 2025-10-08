package fourpetals.com.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/manager")
public class ManagerController {
	@GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        return "manager/dashboard";
    }


}
