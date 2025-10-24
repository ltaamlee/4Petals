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
import org.springframework.transaction.annotation.Transactional;
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

	@Transactional(readOnly = true)
	@GetMapping("/{id}")
	public String productDetailPage(@PathVariable("id") Integer id,
	        @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

	    CustomerRank rank = null;
	    User currentUser = null;

	    if (userDetails != null) {
	        currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
	        if (currentUser != null && currentUser.getKhachHang() != null) {
	            rank = currentUser.getKhachHang().getHangThanhVien();
	        }
	    }

	    Optional<Product> productOpt = productService.findByIdWithMaterials(id);
	    if (productOpt.isEmpty()) {
	        model.addAttribute("errorMessage", "Sản phẩm không tồn tại!");
	        return "customer/product-detail";
	    }

	    Product product = productOpt.get();
	    ProductDetailResponse resp = productService.toResponse(product, rank);

	    productService.increaseViewCount(id);

	    List<Review> reviews = reviewService.getReviewsByProduct(product);
	    Double avgRating = reviewService.getAverageRating(product.getMaSP());

	    // 🔹 Lấy danh sách sản phẩm liên quan
	    List<Product> relatedProducts = productService.getRelatedProducts(
	            product.getDanhMuc().getMaDM(), product.getMaSP());

	    final CustomerRank finalRank = (rank != null) ? rank : CustomerRank.THUONG;
	    List<ProductDetailResponse> relatedProductDtos = relatedProducts.stream()
	            .map(p -> productService.toResponse(p, finalRank))
	            .toList();

	    model.addAttribute("product", resp);
	    model.addAttribute("user", currentUser);
	    model.addAttribute("categories", categoryService.getAllCategories());
	    model.addAttribute("avgRating", avgRating);
	    model.addAttribute("reviews", reviews);
	    model.addAttribute("relatedProducts", relatedProductDtos);

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

	@PostMapping("/add-to-cart")
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