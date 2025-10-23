package fourpetals.com.controller.customer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.Promotion;
import fourpetals.com.entity.Review;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.enums.RoleName;
import fourpetals.com.service.ProductService;
import fourpetals.com.service.PromotionService;
import fourpetals.com.service.ReviewService;
import fourpetals.com.service.CartService;
import fourpetals.com.service.CategoryService;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/{id}")
	public String productDetailPage(@PathVariable("id") Integer id,
			@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		// Lấy thông tin người dùng (nếu có)
		CustomerRank rank = null;
		User currentUser = null;

		if (userDetails != null) {
			currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
			if (currentUser != null && currentUser.getKhachHang() != null) {
				rank = currentUser.getKhachHang().getHangThanhVien();
			}
		}

		// Lấy sản phẩm theo id (có nguyên liệu)
		Optional<Product> productOpt = productService.findByIdWithMaterials(id);
		if (productOpt.isEmpty()) {
			model.addAttribute("errorMessage", "Sản phẩm không tồn tại!");
			return "customer/product-detail"; // vẫn load view nhưng hiển thị lỗi
		}

		Product product = productOpt.get();

		// Chuyển sang DTO để đưa ra view
		ProductDetailResponse resp = productService.toResponse(product);

		// Gán khuyến mãi nếu có
		promotionService.getActivePromotionForProduct(product.getMaSP(), rank).ifPresent(promo -> {
			resp.setBannerKhuyenMai(promo.getTenkm());
			if (promo.getGiaTri() != null) {
				BigDecimal newPrice = product.getGia().subtract(promo.getGiaTri());
				resp.setGiaSauKhuyenMai(newPrice.compareTo(BigDecimal.ZERO) > 0 ? newPrice : BigDecimal.ZERO);
			}
		});

		// Tăng lượt xem
		productService.increaseViewCount(id);

		// Lấy review và đánh giá trung bình
		List<Review> reviews = reviewService.getReviewsByProduct(product);
		Double avgRating = reviewService.getAverageRating(product.getMaSP());

		// Lấy sản phẩm liên quan
		List<Product> relatedProducts = productService.getRelatedProducts(product.getDanhMuc().getMaDM(),
				product.getMaSP());

		// Gán dữ liệu ra view
		model.addAttribute("product", resp);
		model.addAttribute("user", currentUser);
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("avgRating", avgRating);
		model.addAttribute("reviews", reviews);
		model.addAttribute("relatedProducts", relatedProducts);

		return "customer/product-detail";
	}

	@PostMapping("/{id}/review")
	public String addReview(@PathVariable("id") Integer productId, @RequestParam("rating") Integer rating,
			@RequestParam("comment") String comment, Principal principal) {
		User user = userService.findByEmail(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));
		Product product = productService.getProductById(productId);
		reviewService.addReview(product, user, rating, comment);
		return "redirect:/product/" + productId;
	}

	@GetMapping("/buy-now/{id}")
	public String buyNow(@PathVariable("id") Integer id,
			@RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
		return "redirect:/checkout?productId=" + id + "&quantity=" + quantity;
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String> addToCart(@RequestParam("productId") Integer productId,
			@RequestParam("quantity") Integer quantity, Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(401).body("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng!");
		}

		User user = userService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
		cartService.addToCart(user, productId, quantity);
		return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng!");
	}

	// 🔹 Mua ngay
	@GetMapping("/buy-now/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public String buyNow(@PathVariable("id") Integer id,
			@RequestParam(name = "quantity", defaultValue = "1") Integer quantity, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		return "redirect:/checkout?productId=" + id + "&quantity=" + quantity;
	}

	@GetMapping("/check-buy")
	@ResponseBody
	public ResponseEntity<String> checkBuyPermission(Principal principal) {
		if (principal == null) {
			return ResponseEntity.status(401).body("Bạn cần đăng nhập để mua hàng!");
		}

		User user = userService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

		// 🔹 So sánh bằng enum trực tiếp
		if (user.getRole().getRoleName() != RoleName.CUSTOMER) {
			return ResponseEntity.status(403).body("Tài khoản của bạn không được phép mua hàng!");
		}

		return ResponseEntity.ok("OK");
	}

}