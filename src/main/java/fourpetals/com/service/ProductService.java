package fourpetals.com.service;

import java.util.List;
import fourpetals.com.entity.Product;

public interface ProductService {
  List<Product> search(String q, Integer catId, Integer supId);
  Product save(Product p);
  Product findById(Integer id);
  void delete(Integer id);
  long total();
}
