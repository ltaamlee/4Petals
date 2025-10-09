package fourpetals.com.controller.customer;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fourpetals.com.entity.Customer;
import fourpetals.com.service.CustomerService;

@Controller
public class AccountController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/account")
    public String showAccountPage(Model model, Principal principal) {
    	if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Optional<Customer> customerOpt = customerService.findByUser_Username(username);

        if (customerOpt.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("customer", customerOpt.get());
        return "customer/account";
    
    }
}