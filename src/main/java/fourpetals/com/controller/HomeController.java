package fourpetals.com.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.repository.UserRepository;
import fourpetals.com.service.CategoryService;
import fourpetals.com.service.ProductService;
import fourpetals.com.service.UserService;

@Controller
public class HomeController {

	private UserService userService;
	private CategoryService categoryService;
	private ProductService productService;

	public HomeController(UserService userService, CategoryService categoryService, ProductService productService) {
		super();
		this.userService = userService;
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "4Petals Flower Shop");
		return "index";
	}

	@GetMapping("/home")
	public String homePage(Model model, Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();

			User user = userService.findByUsername(username).orElse(null);

			model.addAttribute("username", username);
			model.addAttribute("user", user);
		} else {
			model.addAttribute("username", null);
			model.addAttribute("user", null);
		}
		// 🔹 Lấy 5 sản phẩm khuyến mãi hời nhất
		/* List<Product> bestDeals = productService.findTop5BestDeals(); */

		List<Product> topViewed = productService.getTop10ViewedProducts();
		/*
		 * model.addAttribute("bestDeals", bestDeals);
		 */
		model.addAttribute("topSelling", topViewed);
		return "customer/home";
	}

	@GetMapping("/product")
	public String productPage(@RequestParam(value = "q", required = false) String keyword,
			@RequestParam(value = "categoryIds", required = false) List<Integer> categoryIds,
			@RequestParam(value = "sort", required = false) String sort, // 🆕 thêm tham số sort
			Model model, Authentication authentication) {

		// Thêm user (nếu có đăng nhập)
		addUserToModel(model, authentication);
		model.addAttribute("categories", categoryService.getAllCategories());

		// 🧩 Lọc sản phẩm theo danh mục / keyword
		List<Product> products;
		if ((categoryIds != null && !categoryIds.isEmpty()) || (keyword != null && !keyword.isBlank())) {
			products = productService.searchAndFilter(keyword, categoryIds);
		} else {
			products = productService.getAllProducts();
		}

		// 🧩 Sắp xếp danh sách sản phẩm
		if (sort != null) {
			switch (sort) {
			case "asc": // Giá tăng dần
				products.sort(Comparator.comparing(Product::getGia));
				break;
			case "desc": // Giá giảm dần
				products.sort(Comparator.comparing(Product::getGia).reversed());
				break;
			case "newest": // Mới nhất
				// Nếu Product có trường ngayTao thì sort theo nó, nếu không thì tạm sort theo
				// mã sản phẩm giảm dần
				products.sort(Comparator.comparing(Product::getMaSP).reversed());
				break;
			}
		}

		// 🧩 Truyền dữ liệu về View
		model.addAttribute("products", products);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedCategories", categoryIds == null ? List.of() : categoryIds);
		model.addAttribute("sort", sort); // 🆕 để Thymeleaf giữ lại lựa chọn sort

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
	public String returnPolicy() { return "customer/return-policy"; }

	@GetMapping("/delivery-policy")
	public String deliveryPolicy() { return "customer/delivery-policy"; }

	@GetMapping("/payment-guide")
	public String paymentGuide() { return "customer/payment-guide"; }
}
