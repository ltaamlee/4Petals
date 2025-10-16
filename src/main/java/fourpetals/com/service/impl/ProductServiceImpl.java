package fourpetals.com.service.impl;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.ProductMaterialRepository;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMaterialRepository productMaterialRepository;

	@Autowired
	private ProductRepository productRepo;

	@Override
	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public Product get(Integer id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
	}

	@Override
	public Product create(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product update(Integer id, Product product) {
		Product existing = get(id);
		existing.setTenSP(product.getTenSP());
		existing.setDonViTinh(product.getDonViTinh());
		existing.setDanhMuc(product.getDanhMuc());
		existing.setGia(product.getGia());
		return productRepository.save(existing);
	}

	@Override
	public void delete(Integer id) {
		// Xóa liên kết ProductMaterial trước
		productMaterialRepository.deleteByMaSP_MaSP(id);
		productRepository.deleteById(id);

	}

	@Override
	public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

	@Override
	public Product getProductById(Integer id) {
		return productRepo.findById(id).orElse(null);
	}

	@Override
	public void increaseViewCount(Integer id) {
		Product p = getProductById(id);
		p.setLuotXem(p.getLuotXem() + 1);
		productRepo.save(p);
	}

	/*
	 * @Override public List<Product> getRelatedProducts(Integer maDM, Integer maSP)
	 * { return productRepo.findByDanhMuc_MaDMAndMaSPNot(maDM, maSP); }
	 */

	@Override
	public Product saveProduct(Product product) {
		product.updateStatusBasedOnStock();
		return productRepo.save(product);
	}

	@Override
	public List<Product> searchByName(String keyword) {
		return productRepo.findAll().stream().filter(p -> p.getTenSP().toLowerCase().contains(keyword.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> getTopViewed(int limit) {
		return productRepo.findAll().stream().sorted((a, b) -> b.getLuotXem().compareTo(a.getLuotXem())).limit(limit)
				.collect(Collectors.toList());
	}

}
