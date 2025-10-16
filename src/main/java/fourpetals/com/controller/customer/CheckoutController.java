package fourpetals.com.controller.customer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.entity.Cart;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
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

	@GetMapping
	public String checkoutPage(@RequestParam(required = false) Integer productId,
	                           Model model, Principal principal) {
	    if (principal == null) return "redirect:/login";

	    Customer customer = customerService.findByUsername(principal.getName())
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
	    User user = customer.getUser();

	    List<Cart> cartItems;
	    BigDecimal total;

	    if (productId != null) {
	        // Nếu là "mua ngay"
	        Product product = productService.getProductById(productId);
	        if (product == null) throw new RuntimeException("Sản phẩm không tồn tại");

	        Cart temp = new Cart();
	        temp.setSanPham(product);
	        temp.setSoLuong(1);
	        temp.setTongTien(product.getGia());
	        cartItems = List.of(temp);
	        total = product.getGia();
	    } else {
	        // Nếu mua từ giỏ hàng
	        cartItems = cartService.getCartByUser(user);
	        total = BigDecimal.valueOf(cartService.getTotal(user));
	    }

	    model.addAttribute("customer", customer);
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

		// Sau khi tạo xong -> chuyển sang trang "Đặt hàng thành công"
		return "redirect:/success";
	}
}
