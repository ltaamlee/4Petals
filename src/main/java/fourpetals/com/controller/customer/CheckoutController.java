package fourpetals.com.controller.customer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.response.customers.MomoCreateResponseDto;
import fourpetals.com.entity.*;
import fourpetals.com.enums.PaymentMethod;
import fourpetals.com.enums.PaymentStatus;
import fourpetals.com.service.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	@Autowired private CartService cartService;
	@Autowired private ProductService productService;
	@Autowired private AddressService addressService;
	@Autowired private MomoService momoService;

	// === 1. Trang thanh toán (Giữ nguyên) ===
	@GetMapping
	public String checkoutPage(@RequestParam(required = false) Integer productId,
			@RequestParam(required = false, defaultValue = "1") Integer quantity, Model model, Principal principal) {
		if (principal == null)
			return "redirect:/login";

		Customer customer = customerService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
		User user = customer.getUser();

		List<Address> addresses = addressService.findByUsername(principal.getName());
		Address defaultAddress = addressService.findDefaultByUsername(principal.getName());

		List<Cart> cartItems;
		BigDecimal total;

		if (productId != null) {
			Product product = productService.getProductById(productId);
			Cart temp = new Cart();
			temp.setSanPham(product);
			temp.setSoLuong(quantity);
			temp.setTongTien(product.getGia().multiply(BigDecimal.valueOf(quantity)));
			cartItems = List.of(temp);
			total = temp.getTongTien();
		} else {
			cartItems = cartService.getCartByUser(user);
			total = cartItems.stream().map(c -> c.getSanPham().getGia().multiply(BigDecimal.valueOf(c.getSoLuong())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}

		BigDecimal shippingFee = BigDecimal.valueOf(30000);
		BigDecimal grandTotal = total.add(shippingFee);

		model.addAttribute("user", user);
		model.addAttribute("addresses", addresses);
		model.addAttribute("defaultAddress", defaultAddress);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", total);
		model.addAttribute("shippingFee", shippingFee);
		model.addAttribute("grandTotal", grandTotal);

		return "customer/checkout";
	}
	
	// === 2. HÀM SUBMIT FORM CHÍNH (ĐÃ SỬA LẠI) ===
	// Xử lý cả COD và MoMo (QR)
	@PostMapping("/confirm")
	public String confirmOrder(@RequestParam String tenNguoiNhan,
	                           @RequestParam String sdt,
	                           @RequestParam String diaChi,
	                           @RequestParam(required = false) String ghiChu,
	                           @RequestParam String paymentMethod, // Sẽ nhận "COD" hoặc "QR"
	                           @RequestParam(required = false) Integer productId,
	                           @RequestParam(required = false, defaultValue = "1") Integer quantity,
	                           Principal principal) {

	    if (principal == null) return "redirect:/login";

	    Customer customer = customerService.findByUsername(principal.getName())
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

	    Order order;

	    // 1. ⚙️ Tạo đối tượng Order
	    if (productId != null) {
	        Product product = productService.getProductById(productId);
	        if (product == null) throw new RuntimeException("Sản phẩm không tồn tại");
	        order = orderService.createOrder(customer, product, quantity, tenNguoiNhan, sdt, diaChi, ghiChu);
	    } else {
	        order = orderService.createOrder(customer, tenNguoiNhan, sdt, diaChi, ghiChu);
	    }

	    Order savedOrder; // Biến để hứng đơn hàng đã có maDH

	    // 2. 🚀 Xử lý COD
	    if (paymentMethod.equalsIgnoreCase("COD")) {
	        order.setPhuongThucThanhToan(PaymentMethod.COD);
	        order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
	        savedOrder = orderService.save(order);
	        cartService.clearCart(customer.getUser());
	        return "redirect:/checkout/success";
	    }

	    // 3. 💳 Xử lý MoMo (QR)
	    if (paymentMethod.equalsIgnoreCase("QR")) {
	        order.setPhuongThucThanhToan(PaymentMethod.MOMO);
	        order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
	        savedOrder = orderService.save(order); // Lưu đơn trước để lấy maDH

	        try {
	            // Gọi MoMo service bằng "savedOrder" (đã có maDH)
	            MomoCreateResponseDto momoResponse = momoService.createPayment(savedOrder);

	            // Kiểm tra MoMo trả về OK và có link
	            if (momoResponse.getResultCode() == 0 && momoResponse.getPayUrl() != null) {
	                // ❗️ CHUYỂN HƯỚNG NGƯỜI DÙNG TỚI TRANG CỦA MOMO
	                return "redirect:" + momoResponse.getPayUrl();
	            } else {
	                // Nếu MoMo báo lỗi (trùng orderId, sai key...)
	                System.err.println("❌ Lỗi từ MoMo: " + momoResponse.getMessage());
	                return "redirect:/checkout/error?msg=MomoError";
	            }
	        } catch (Exception e) {
	            System.err.println("❌ Lỗi nghiêm trọng khi gọi MoMo: " + e.getMessage());
	            return "redirect:/checkout/error?msg=Exception";
	        }
	    }

	    // Nếu paymentMethod không phải COD hay QR
	    return "redirect:/checkout/error?msg=InvalidMethod";
	}


	// File: CheckoutController.java
	// ... (các hàm khác giữ nguyên)

	// === 4. HÀM XỬ LÝ KHI MOMO TRẢ VỀ (Return URL) ===
	@GetMapping("/momo/return")
	public String handleMomoReturn(@RequestParam(name = "resultCode") int resultCode, // Nhận là int
	                               @RequestParam(name = "orderId") String orderId) {
	   
	    boolean isSuccess = momoService.handleMomoReturn(orderId, resultCode);

	    if (isSuccess) {
	        // Thanh toán thành công (resultCode == 0)
	        return "redirect:/checkout/success";
	    } else {
	        // Thất bại hoặc hủy (service đã xóa đơn hàng)
	        return "redirect:/checkout/error";
	    }
	}
	// ✅ Trang đặt hàng thành công
	@GetMapping("/success")
	public String checkoutSuccess() {
	    return "customer/success";
	}

	// ❌ Trang lỗi thanh toán
	@GetMapping("/error")
	public String checkoutError() {
	    return "customer/checkout-error";
	}
}