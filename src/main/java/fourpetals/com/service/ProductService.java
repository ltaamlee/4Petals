// fourpetals/com/service/ProductService.java
package fourpetals.com.service;

import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Product;
import fourpetals.com.model.ProductRowVM;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductRowVM> searchNoPaging(String keyword, Integer status, Integer categoryId);

    Optional<ProductDetailResponse> getDetail(Integer id);

    Integer create(ProductRequest req); // trả về id

    void update(Integer id, ProductRequest req);

    ProductDetailResponse findById(Integer maSP);  // Giữ method này như bạn đã khai báo

    void delete(Integer maSP);

    // Add
    List<Product> getAllProducts();

    Product getProductById(Integer id);

    void increaseViewCount(Integer id);

    Product saveProduct(Product product);

    List<Product> searchByName(String keyword);

    List<Product> getTopViewed(int limit);
}
