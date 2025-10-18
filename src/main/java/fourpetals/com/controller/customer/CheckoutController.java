package fourpetals.com.controller.customer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Address;
import fourpetals.com.entity.Cart;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.service.AddressService;
import fourpetals.com.service.CartService;
import fourpetals.com.service.CustomerService;
import fourpetals.com.service.OrderService;
import fourpetals.com.service.ProductService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductService productService;
	@Autowired
    private AddressService addressService;

	@GetMapping
	public String checkoutPage(@RequestParam(required = false) Integer productId,
	                           @RequestParam(required = false, defaultValue = "1") Integer quantity,
	                           Model model, Principal principal) {
	    if (principal == null) return "redirect:/login";

	    Customer customer = customerService.findByUsername(principal.getName())
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
	    List<Address> addresses = addressService.findByUsername(principal.getName());
	    User user = customer.getUser();

	    List<Cart> cartItems;
	    BigDecimal total;

	    if (productId != null) {
	        Product product = productService.getProductById(productId);
	        if (product == null) throw new RuntimeException("Sản phẩm không tồn tại");

	        Cart temp = new Cart();
	        temp.setSanPham(product);
	        temp.setSoLuong(quantity);
	        temp.setTongTien(product.getGia().multiply(BigDecimal.valueOf(quantity)));
	        cartItems = List.of(temp);
	        total = product.getGia().multiply(BigDecimal.valueOf(quantity));
	    } else {
	        cartItems = cartService.getCartByUser(user);
	        total = cartItems.stream()
	                .map(c -> c.getSanPham().getGia().multiply(BigDecimal.valueOf(c.getSoLuong())))
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
	    }

	    model.addAttribute("customer", customer);
	    model.addAttribute("user", user);
	    model.addAttribute("addresses", addresses);
	    System.out.println(">>> Số địa chỉ lấy ra: " + addresses.size());
	    model.addAttribute("cartItems", cartItems);
	    model.addAttribute("total", total);
	    return "customer/checkout";
	}



	// Xác nhận đặt hàng
	@PostMapping("/confirm")
	public String confirmOrder(@RequestParam String tenNguoiNhan, @RequestParam String sdt, @RequestParam String diaChi,
			@RequestParam(required = false) String ghiChu, Principal principal) {

		if (principal == null)
			return "redirect:/login";

		Customer customer = customerService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

		// Tạo đơn hàng từ giỏ hàng hiện tại
		orderService.createOrder(customer, tenNguoiNhan, sdt, diaChi, ghiChu);
		cartService.clearCart(customer.getUser());

		// Sau khi tạo xong -> chuyển sang trang "Đặt hàng thành công"
		return "redirect:/success";
	}
}
