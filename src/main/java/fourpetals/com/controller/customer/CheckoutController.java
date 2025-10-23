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
import jakarta.servlet.http.HttpServletRequest;

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
	@Autowired
	private MomoService momoService;

	// === 1. Trang thanh toán ===
	@GetMapping
	public String checkoutPage(@RequestParam(required = false) String selectedIds,
			@RequestParam(required = false) Integer productId,
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
			// ✅ Mua ngay 1 sản phẩm
			Product product = productService.getProductById(productId);
			Cart temp = new Cart();
			temp.setSanPham(product);
			temp.setSoLuong(quantity);
			temp.setTongTien(product.getGia().multiply(BigDecimal.valueOf(quantity)));
			cartItems = List.of(temp);
			total = temp.getTongTien();
		} else if (selectedIds != null && !selectedIds.isBlank()) {
			// ✅ Mua các sản phẩm được tick trong giỏ
			List<Integer> ids = List.of(selectedIds.split(",")).stream().map(Integer::parseInt).toList();
			cartItems = cartService.getCartByIds(ids);
			total = cartItems.stream().map(c -> c.getSanPham().getGia().multiply(BigDecimal.valueOf(c.getSoLuong())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		} else {
			// ⚠️ fallback: thanh toán toàn bộ giỏ nếu không tick gì
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
		model.addAttribute("productId", productId);
		model.addAttribute("quantity", quantity);
		model.addAttribute("selectedIds", selectedIds);


		System.out.println("🧾 Selected IDs = " + selectedIds);

		return "customer/checkout";
	}

	// === 2. Xử lý đặt hàng ===
	@PostMapping("/confirm")
	public String confirmOrder(@RequestParam String tenNguoiNhan, @RequestParam String sdt, @RequestParam String diaChi,
			@RequestParam(required = false) String ghiChu, @RequestParam String paymentMethod,
			@RequestParam(required = false) Integer productId,
			@RequestParam(required = false, defaultValue = "1") Integer quantity, Principal principal,
			HttpServletRequest request) {

		if (principal == null)
			return "redirect:/login";

		Customer customer = customerService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

		Order order;

		// 🛍️ 1. Tạo đơn hàng mới
		if (productId != null) {
			// ✅ Mua ngay 1 sản phẩm (KHÔNG động tới giỏ)
			Product product = productService.getProductById(productId);
			if (product == null)
				throw new RuntimeException("Sản phẩm không tồn tại");
			order = orderService.createOrder(customer, product, quantity, tenNguoiNhan, sdt, diaChi, ghiChu);
			// Không lưu selectedIds → không xóa gì trong giỏ
			request.getSession().removeAttribute("selectedIds");
		} else {
			// ✅ Mua từ giỏ hàng
			String selectedIds = request.getParameter("selectedIds");
			if (selectedIds != null && !selectedIds.isBlank()) {
				List<Integer> ids = List.of(selectedIds.split(",")).stream().map(Integer::parseInt).toList();
				order = orderService.createOrder(customer, tenNguoiNhan, sdt, diaChi, ghiChu, ids);
				// ✅ Lưu lại danh sách tick để xóa đúng sp
				request.getSession().setAttribute("selectedIds", selectedIds);
			} else {
				throw new RuntimeException("Không có sản phẩm nào được chọn để thanh toán!");
			}
		}

		// 🚀 2. Thanh toán COD
		if (paymentMethod.equalsIgnoreCase("COD")) {
			order.setPhuongThucThanhToan(PaymentMethod.COD);
			order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
			orderService.save(order);
			return "redirect:/checkout/success";
		}

		// 💳 3. Thanh toán MoMo
		if (paymentMethod.equalsIgnoreCase("QR")) {
			order.setPhuongThucThanhToan(PaymentMethod.MOMO);
			order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
			orderService.save(order);

			try {
				MomoCreateResponseDto momoResponse = momoService.createPayment(order);
				if (momoResponse.getResultCode() == 0 && momoResponse.getPayUrl() != null) {
					if (productId == null)
						request.getSession().setAttribute("selectedIds", request.getParameter("selectedIds"));
					return "redirect:" + momoResponse.getPayUrl();
				} else {
					System.err.println("❌ Lỗi từ MoMo: " + momoResponse.getMessage());
					return "redirect:/checkout/error?msg=MomoError";
				}
			} catch (Exception e) {
				System.err.println("❌ Lỗi khi gọi MoMo: " + e.getMessage());
				return "redirect:/checkout/error?msg=Exception";
			}
		}

		return "redirect:/checkout/error?msg=InvalidMethod";
	}

	// === 3. Xử lý khi MoMo trả về ===
	@GetMapping("/momo/return")
	public String handleMomoReturn(@RequestParam(name = "resultCode") int resultCode,
			@RequestParam(name = "orderId") String orderId, HttpServletRequest request, Principal principal) {

		boolean isSuccess = momoService.handleMomoReturn(orderId, resultCode);
		if (isSuccess) {
			removePurchasedItems(request, principal);
			return "redirect:/checkout/success";
		}
		return "redirect:/checkout/error";
	}

	// === 4. Trang đặt hàng thành công ===
	@GetMapping("/success")
	public String checkoutSuccess(HttpServletRequest request, Principal principal) {
		removePurchasedItems(request, principal);
		return "customer/success";
	}

	// === 5. Trang lỗi thanh toán ===
	@GetMapping("/error")
	public String checkoutError() {
		return "customer/checkout-error";
	}

	// === 6. Xóa sp đã mua trong giỏ ===
	private void removePurchasedItems(HttpServletRequest request, Principal principal) {
	    String selectedIds = (String) request.getSession().getAttribute("selectedIds");

	    if (selectedIds != null && principal != null) {
	        List<Integer> ids = List.of(selectedIds.split(","))
	                                .stream()
	                                .map(String::trim)
	                                .filter(s -> !s.isEmpty())
	                                .map(Integer::parseInt)
	                                .toList();

	        // Lấy user hiện tại
	        User currentUser = customerService.findByUsername(principal.getName())
	                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"))
	                .getUser();

	        // ✅ Xóa có chọn lọc
	        cartService.clearCart(currentUser, ids);

	        // Xóa khỏi session
	        request.getSession().removeAttribute("selectedIds");
	    }
	}



}
