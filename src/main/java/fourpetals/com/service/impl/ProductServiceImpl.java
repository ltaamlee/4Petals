package fourpetals.com.service.impl;

import fourpetals.com.entity.Product;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Override
	public Page<Product> list(String q, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product create(Product p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product update(Integer id, Product p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

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
