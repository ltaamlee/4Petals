package fourpetals.com.service;

import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.model.ProductRowVM;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface ProductService {

	void updateImage(Integer maSP, MultipartFile file);

	List<ProductRowVM> searchNoPaging(String keyword, Integer status, Integer categoryId);

	Optional<ProductDetailResponse> getDetail(Integer id);

	Integer create(ProductRequest req); // trả về id

	void update(Integer id, ProductRequest req);

	ProductDetailResponse findById(Integer maSP); // Giữ method này như bạn đã khai báo

	void delete(Integer maSP);

	// Add
	List<Product> getAllProducts();

	Product getProductById(Integer id);

	void increaseViewCount(Integer productId);

	Product saveProduct(Product product);

	List<Product> searchByName(String keyword);

	List<Product> searchAndFilter(String keyword, List<Integer> categoryIds);

	List<Product> getTopViewed(int limit);

	ProductDetailResponse create(ProductRequest req, MultipartFile file);

	ProductDetailResponse update(Integer maSP, ProductRequest req, MultipartFile file);

	ProductDetailResponse toResponse(Product product);

	ProductDetailResponse getDetailWithMaterials(Integer id);

	/* List<Product> findTop5BestDeals(); */

	List<Product> getTop10ViewedProducts();

	List<Product> getRelatedProducts(Integer categoryId, Integer currentProductId);

	
	
	//Khuyến mãi
	List<Product> findAllWithMaterials();
	
	Optional<Product> findByIdWithMaterials(Integer id);

}
