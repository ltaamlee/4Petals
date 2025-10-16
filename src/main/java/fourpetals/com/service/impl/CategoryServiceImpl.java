// fourpetals/com/service/impl/CategoryServiceImpl.java
package fourpetals.com.service.impl;

import fourpetals.com.dto.request.categories.CategoryRequest;
import fourpetals.com.entity.Category;
import fourpetals.com.model.CategoryRowVM;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository repo;

	private CategoryRowVM toVM(Category c) {
		return new CategoryRowVM(c.getMaDM(), c.getTenDM(), c.getMoTa(), c.getUpdatedAt());
	}

	@Override
	public Page<CategoryRowVM> search(String keyword, int page, int size, Sort sort) {
		Pageable pageable = PageRequest.of(page, size, sort == null ? Sort.by("maDM").ascending() : sort);
		return repo.search(keyword == null ? "" : keyword.trim(), pageable).map(this::toVM);
	}

	@Override
	public java.util.Optional<CategoryRowVM> get(Integer id) {
		return repo.findById(id).map(this::toVM);
	}

	@Override
	@Transactional
	public CategoryRowVM create(CategoryRequest req) {
		Category c = new Category();
		c.setTenDM(req.tenDM().trim());
		c.setMoTa(req.moTa());
		repo.save(c);
		return toVM(c);
	}

	@Override
	@Transactional
	public CategoryRowVM update(Integer id, CategoryRequest req) {
		Category c = repo.findById(id).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
		if (req.tenDM() != null)
			c.setTenDM(req.tenDM().trim());
		c.setMoTa(req.moTa());
		repo.save(c);
		return toVM(c);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if (!repo.existsById(id))
			throw new RuntimeException("Danh mục không tồn tại");
		repo.deleteById(id);
	}
	
	@Override
	public List<CategoryRowVM> getAllCategories() {
	    return repo.findAll()
	               .stream()
	               .map(c -> new CategoryRowVM(c.getMaDM(), c.getTenDM(), c.getMoTa(), c.getUpdatedAt()))
	               .toList();
	}

}
