package fourpetals.com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fourpetals.com.entity.Product;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repo;

  public ProductServiceImpl(ProductRepository repo) {
    this.repo = repo;
  }

  @Override public List<Product> search(String q, Integer catId, Integer supId) {
    return repo.search(q, catId, supId);
  }

  @Override public Product save(Product p) {
    p.updateStatusBasedOnStock();
    return repo.save(p);
  }

  @Override public Product findById(Integer id) {
    return repo.findById(id).orElse(null);
  }

  @Override public void delete(Integer id) {
    repo.deleteById(id);
  }

  @Override public long total() {
    return repo.total();
  }
}
