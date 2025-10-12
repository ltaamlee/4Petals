package fourpetals.com.service.impl;

import fourpetals.com.entity.Product;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

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

	
}
