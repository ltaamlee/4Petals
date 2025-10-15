package fourpetals.com.service;

import fourpetals.com.dto.request.categories.CategoryRequest;
import fourpetals.com.model.CategoryRowVM;
import org.springframework.data.domain.*;

import java.util.Optional;

public interface CategoryService {
    Page<CategoryRowVM> search(String keyword, int page, int size, Sort sort);
    Optional<CategoryRowVM> get(Integer id);
    CategoryRowVM create(CategoryRequest req);
    CategoryRowVM update(Integer id, CategoryRequest req);
    void delete(Integer id);
}
