package fourpetals.com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import fourpetals.com.entity.Product;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ProductService {
	Page<Product> list(String q, Pageable pageable);

	Product get(Integer id);

	Product create(Product p);

	Product update(Integer id, Product p);

	void delete(Integer id);

	List<Product> getAllProducts();

	Product getProductById(Integer id);

	void increaseViewCount(Integer id);

	/* List<Product> getRelatedProducts(Integer maDM, Integer maSP); */

	Product saveProduct(Product product);

	List<Product> searchByName(String keyword);

	List<Product> getTopViewed(int limit);
}
