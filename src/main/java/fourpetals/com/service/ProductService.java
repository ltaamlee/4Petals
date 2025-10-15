package fourpetals.com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(Integer id);

    Product get(Integer id);

    Product create(Product product);

    Product update(Integer id, Product product);

    void delete(Integer id);

}
