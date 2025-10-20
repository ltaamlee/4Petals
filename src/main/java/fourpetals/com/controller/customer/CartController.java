package fourpetals.com.controller.customer;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Cart;
import fourpetals.com.entity.User;
import fourpetals.com.service.CartService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<Cart> items = cartService.getCartByUser(user);
        Double total = cartService.getTotal(user);
        
        model.addAttribute("user", user);
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "customer/cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateQuantity(@RequestParam("id")  Integer id, @RequestParam("quantity") Integer quantity) {
        cartService.updateQuantity(id, quantity);
        return "OK";
    }

    @PostMapping("/remove")
    @ResponseBody
    public String removeItem(@RequestParam("id") Integer cartId) {
        cartService.removeItem(cartId);
        return "ok";
    }

    
    @GetMapping("/count")
    @ResponseBody
    public Map<String, Object> getCartCount(Principal principal) {
        int count = 0;
        if (principal != null) {
            User user = userService.findByUsername(principal.getName())
                    .orElse(null);
            if (user != null)
                count = cartService.getCartCount(user);
        }
        return Map.of("count", count);
    }

}
