package fourpetals.com.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	    User currentUser = null;
	    CustomerRank rank = null;

	    if (authentication != null && authentication.isAuthenticated()) {
	        String username = authentication.getName();
	        currentUser = userService.findByUsername(username).orElse(null);
	        if (currentUser != null && currentUser.getKhachHang() != null) {
	            rank = currentUser.getKhachHang().getHangThanhVien();
	        }
	        model.addAttribute("username", username);
	        model.addAttribute("user", currentUser);
	    } else {
	        model.addAttribute("username", null);
	        model.addAttribute("user", null);
	    }

	    // ✅ Lấy danh sách khuyến mãi (dạng ProductDetailResponse)
	    List<ProductDetailResponse> promoProducts = productService.getTopPromotionalProducts(rank);
	    model.addAttribute("promoProducts", promoProducts);

	    // ✅ Lấy sản phẩm xem nhiều nhất
	    List<ProductDetailResponse> topViewed = productService.getTopViewedProductsWithPromo(rank);
	    model.addAttribute("topSelling", topViewed);


	    return "customer/home";
	}


	@GetMapping("/product")
	public String productPage(
	        @RequestParam(value = "q", required = false) String keyword,
	        @RequestParam(value = "categoryIds", required = false) List<Integer> categoryIds,
	        @RequestParam(value = "sort", required = false) String sort,
	        Model model,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {

	    // 🔹 Lấy user & rank
	    CustomerRank rank = null;
	    User currentUser = null;

	    if (userDetails != null) {
	        currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
	        if (currentUser != null && currentUser.getKhachHang() != null) {
	            rank = currentUser.getKhachHang().getHangThanhVien();
	        }
	    }

	    // 🔹 Lọc sản phẩm
	    List<Product> products;
	    if ((categoryIds != null && !categoryIds.isEmpty()) || (keyword != null && !keyword.isBlank())) {
	        products = productService.searchAndFilter(keyword, categoryIds);
	    } else {
	        products = productService.findAllWithMaterials();
	    }

	    CustomerRank finalRank = rank;

	    // 🔹 Map sang DTO + tính giá sau KM trước khi sort
	    List<ProductDetailResponse> productResponses = products.stream().map(p -> {
	        ProductDetailResponse resp = productService.toResponse(p, finalRank);

	        promotionService.getActivePromotionForProduct(p.getMaSP(), finalRank).ifPresent(promo -> {
	            if (promo.getLoaiKm().name().equals("PERCENT") && promo.getGiaTri() != null) {
	                BigDecimal percent = promo.getGiaTri();
	                BigDecimal discount = p.getGia()
	                        .multiply(percent)
	                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
	                BigDecimal giaSauKM = p.getGia().subtract(discount);

	                resp.setGiaSauKhuyenMai(giaSauKM);
	                resp.setGiamPhanTram(percent.intValue());
	                resp.setBannerKhuyenMai(promo.getTenkm());
	                resp.setLoaiKhuyenMai("PERCENT");
	            } else {
	                // Nếu không có KM thì set giá sau KM = giá gốc để sort ổn định
	                resp.setGiaSauKhuyenMai(p.getGia());
	            }
	        });

	        // Nếu không có khuyến mãi nào áp dụng, vẫn set giáSauKhuyenMai = giá gốc
	        if (resp.getGiaSauKhuyenMai() == null) {
	            resp.setGiaSauKhuyenMai(p.getGia());
	        }

	        return resp;
	    }).collect(Collectors.toList());

	    // 🔹 Sort theo giá sau khuyến mãi (nếu có)
	    if (sort != null) {
	        Comparator<ProductDetailResponse> byPrice = Comparator.comparing(
	            ProductDetailResponse::getGiaSauKhuyenMai,
	            Comparator.nullsLast(BigDecimal::compareTo)
	        );

	        switch (sort) {
	            case "asc" -> productResponses.sort(byPrice);
	            case "desc" -> productResponses.sort(byPrice.reversed());
case "newest" -> productResponses.sort(Comparator.comparing(ProductDetailResponse::getMaSP).reversed());
	        }
	    }



	    // 🔹 Truyền dữ liệu về view
	    model.addAttribute("categories", categoryService.getAllCategories());
	    model.addAttribute("products", productResponses);
	    model.addAttribute("user", currentUser);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("selectedCategories", categoryIds == null ? List.of() : categoryIds);
	    model.addAttribute("sort", sort);

	    return "customer/product";
	}

	@GetMapping("/contact")
	public String contact(Model model, Authentication authentication) {
		addUserToModel(model, authentication);
		model.addAttribute("products", productService.getAllProducts());
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

	@GetMapping("/return-policy")
	public String returnPolicy(Model model, Authentication authentication) {
	    addUserToModel(model, authentication);
	    return "customer/return-policy";
	}

	@GetMapping("/delivery-policy")
	public String deliveryPolicy(Model model, Authentication authentication) {
	    addUserToModel(model, authentication);
	    return "customer/delivery-policy";
	}

	@GetMapping("/payment-guide")
	public String paymentGuide(Model model, Authentication authentication) {
	    addUserToModel(model, authentication);
	    return "customer/payment-guide";
	}

}
