package fourpetals.com.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import fourpetals.com.entity.Address;
import fourpetals.com.entity.Customer;
import fourpetals.com.service.AddressService;
import fourpetals.com.service.CustomerService;

import java.security.Principal;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public String addAddress(Address address, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        address.setKhachHang(customer);
        addressService.save(address);
        return "redirect:/checkout"; // quay lại trang thanh toán
    }
}
