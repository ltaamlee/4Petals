package fourpetals.com.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.CategoryService;
import fourpetals.com.service.ProductService;
import fourpetals.com.service.PromotionService;
import fourpetals.com.service.UserService;

@Controller
public class HomeController {

	private final UserService userService;
	private final ProductService productService;
	private final PromotionService promotionService;
	private final CategoryService categoryService;




	public HomeController(UserService userService, ProductService productService, PromotionService promotionService,
			CategoryService categoryService) {
		super();
		this.userService = userService;
		this.productService = productService;
		this.promotionService = promotionService;
		this.categoryService = categoryService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "4Petals Flower Shop");
		return "index";
	}

	@GetMapping("/home")
	public String homePage(Model model, Authentication authentication) {
		addUserToModel(model, authentication);
		return "customer/home";
	}

	@GetMapping("/product")
	public String productPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

	    CustomerRank rank = null;
	    User currentUser = null;

	    if (userDetails != null) {
	        currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
	        if (currentUser != null && currentUser.getKhachHang() != null) {
	            rank = currentUser.getKhachHang().getHangThanhVien();
	        }
	    }

	    // Lấy list product + materials
	    List<Product> productList = productService.findAllWithMaterials();

	    // Biến effectively final để dùng trong lambda
	    CustomerRank finalRank = rank;

	    // Chuyển thành DTO và gán khuyến mãi nếu có
	    List<ProductDetailResponse> products = productList.stream().map(p -> {
	        ProductDetailResponse resp = productService.toResponse(p);

	        promotionService.getActivePromotionForProduct(p.getMaSP(), finalRank).ifPresent(promo -> {
	            resp.setBannerKhuyenMai(promo.getTenkm());
	            if (promo.getGiaTri() != null) {
	                resp.setGiaSauKhuyenMai(p.getGia().subtract(promo.getGiaTri()));
	            }
	        });
	        return resp;
	    }).toList();

	    model.addAttribute("categories", categoryService.getAllCategories());
	    model.addAttribute("products", products);
	    model.addAttribute("user", currentUser);
	    

	    return "customer/product";
	}


	@GetMapping("/contact")
	public String contact(Model model, Authentication authentication) {
		addUserToModel(model, authentication);

		return "customer/contact";
	}

	@GetMapping("/about")
	public String about(Model model, Authentication authentication) {
		addUserToModel(model, authentication);
		return "customer/about";
	}

	private void addUserToModel(Model model, Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			User user = userService.findByUsername(username).orElse(null);
			model.addAttribute("user", user);
		} else {
			model.addAttribute("user", null);
		}
	}
}
