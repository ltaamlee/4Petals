package fourpetals.com.service.impl;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.ProductMaterialRepository;
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

}
