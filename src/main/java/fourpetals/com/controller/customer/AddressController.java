package fourpetals.com.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fourpetals.com.entity.Address;
import fourpetals.com.entity.Customer;
import fourpetals.com.service.AddressService;
import fourpetals.com.service.CustomerService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/account/address") // ✅ khớp với đường dẫn trong sidebar
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    // ✅ Hiển thị danh sách địa chỉ
    @GetMapping
    public String listAddresses(Model model, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        List<Address> addresses = addressService.findByUsername(principal.getName());
        
        model.addAttribute("user", customer.getUser());
        model.addAttribute("customer", customer);
        model.addAttribute("addresses", addresses);
        return "customer/account-address";
    }


    // ✅ Hiển thị form thêm
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("address", new Address());
        return "customer/account-address-form";
    }
    
    @PostMapping("/add")
    public String addAddress(Address address, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        address.setKhachHang(customer);
        addressService.save(address);
        return "redirect:/checkout"; // quay lại trang thanh toán
    }

    
    // ✅ Lưu địa chỉ mới hoặc cập nhật
    @PostMapping("/save")
    public String saveAddress(@ModelAttribute Address address, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        address.setKhachHang(customer);
        addressService.save(address);
        return "redirect:/account/address";
    }

    // ✅ Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String editAddress(@PathVariable("id") Integer id, Model model) {
        Address address = addressService.findById(id);
        model.addAttribute("address", address);
        return "customer/account-address-form";
    }

    // ✅ Xóa
    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer id) {
        addressService.delete(id);
        return "redirect:/account/address";
    }

    // ✅ Thiết lập mặc định
    @GetMapping("/set-default/{id}")
    public String setDefault(@PathVariable("id") Integer id, Principal principal) {
        addressService.setDefault(id, principal.getName());
        return "redirect:/account/address";
    }
}
