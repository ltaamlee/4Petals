package fourpetals.com.service;

import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

	Page<ProductDetailResponse> search(String keyword, Integer status, Integer categoryId, Pageable pageable);

	ProductDetailResponse findById(Integer maSP);

	ProductDetailResponse create(ProductRequest req, MultipartFile file);

	ProductDetailResponse update(Integer maSP, ProductRequest req, MultipartFile file);

	void delete(Integer maSP);

	
	
	
	//    Add 
	List<Product> getAllProducts();

	Product getProductById(Integer id);

	void increaseViewCount(Integer id);

	/* List<Product> getRelatedProducts(Integer maDM, Integer maSP); */

	Product saveProduct(Product product);

	List<Product> searchByName(String keyword);

	List<Product> getTopViewed(int limit);
}
