package fourpetals.com.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.Review;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.service.ProductService;
import fourpetals.com.service.PromotionService;
import fourpetals.com.service.ReviewService;
import fourpetals.com.service.CartService;
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

	
	@GetMapping("/{id}")
	public String detailPage(@PathVariable("id") Integer id, Model model, Principal principal) {
	    Product product = productService.getProductById(id);

	    if (product == null) {
	        return "redirect:/product"; // nếu id sai thì về trang danh sách
	    }

	    ProductStatus status = ProductStatus.fromValue(product.getTrangThai());
	    model.addAttribute("status", status);

	    if (!status.isVisible()) {
	        return "redirect:/";
	    }

	    // Tăng view
	    productService.increaseViewCount(id);

	    // Lấy review
	    Double avgRating = reviewService.getAverageRating(id);
	    List<Review> reviews = reviewService.getReviewsByProduct(product);

	    // Xác định user và rank
	    User currentUser = null;
	    CustomerRank rank = CustomerRank.THUONG; // rank mặc định nếu chưa login

	    if (principal != null) {
	        currentUser = userService.findByUsername(principal.getName()).orElse(null);
	        if (currentUser != null) {
	            model.addAttribute("user", currentUser);
	            if (currentUser.getKhachHang() != null) {
	                rank = currentUser.getKhachHang().getHangThanhVien();
	            }
	        }
	    }

	    // Chuyển Product thành DTO để hiển thị banner
	    ProductDetailResponse resp = productService.toResponse(product);

	    // Lấy khuyến mãi áp dụng theo rank hoặc khuyến mãi chung
	    promotionService.getActivePromotionForProduct(product.getMaSP(), rank)
	                    .ifPresentOrElse(promo -> {
	                        resp.setBannerKhuyenMai(promo.getTenkm());
	                        if (promo.getGiaTri() != null) {
	                            resp.setGiaSauKhuyenMai(product.getGia().subtract(promo.getGiaTri()));
	                        }
	                    }, () -> {
	                        // Nếu không có khuyến mãi theo rank, check khuyến mãi chung
	                        promotionService.getActivePromotionForProduct(product.getMaSP(), null)
	                                        .ifPresent(promo -> {
	                                            resp.setBannerKhuyenMai(promo.getTenkm());
	                                            if (promo.getGiaTri() != null) {
	                                                resp.setGiaSauKhuyenMai(product.getGia().subtract(promo.getGiaTri()));
	                                            }
	                                        });
	                    });

	    model.addAttribute("product", resp);
	    model.addAttribute("avgRating", avgRating);
	    model.addAttribute("reviews", reviews);

	    return "customer/product-detail";
	}


	// 🔹 Gửi đánh giá
	@PostMapping("/{id}/review")
	public String addReview(@PathVariable("id") Integer productId, @RequestParam("rating") Integer rating,
			@RequestParam("comment") String comment, Principal principal) {
		User user = userService.findByEmail(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));
		Product product = productService.getProductById(productId);
		reviewService.addReview(product, user, rating, comment);
		return "redirect:/product/" + productId;
	}

	// 🔹 Thêm vào giỏ hàng
	@PostMapping("/add-to-cart")
	@ResponseBody
	public String addToCart(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity,
			Principal principal) {
		if (principal == null) {
	        return "redirect:/login"; // nếu id sai thì về trang danh sách
	    }
		User user = userService.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
		cartService.addToCart(user, productId, quantity);
		return "Đã thêm vào giỏ hàng!";
	}

	// 🔹 Mua ngay (chuyển sang trang thanh toán)
	@GetMapping("/buy-now/{id}")
	public String buyNow(@PathVariable("id") Integer id,
	                     @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
	    return "redirect:/checkout?productId=" + id + "&quantity=" + quantity;
	}

}
