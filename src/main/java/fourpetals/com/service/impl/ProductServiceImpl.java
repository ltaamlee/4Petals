package fourpetals.com.service.impl;

import fourpetals.com.dto.request.products.ProductMaterialLineRequest;
import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.entity.Category;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.entity.ProductMaterialId;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.ProductMaterialRepository;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductService;
import fourpetals.com.utils.Upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private MaterialRepository materialRepo;
	@Autowired
	private ProductMaterialRepository pmRepo;
	@Autowired
	private Upload upload;


	@Override
	public Page<ProductDetailResponse> search(String keyword, Integer status, Integer categoryId, Pageable pageable) {
		Page<Product> page = productRepo.search(Optional.ofNullable(keyword).orElse(""), status, categoryId, pageable);
		return page.map(this::toResponse);
	}

	@Override
	public ProductDetailResponse findById(Integer maSP) {
		Product p = productRepo.findById(maSP).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		// ensure materials fetched if lazy
		p.getProductMaterials().size();
		return toResponse(p);
	}

	@Override
	@Transactional
	public ProductDetailResponse create(ProductRequest req, MultipartFile file) {
		Product p = new Product();
		mapUpsert(p, req);

		// Upload ảnh nếu có
		if (file != null && !file.isEmpty()) {
			String saved = saveImage(file);
			p.setHinhAnh(saved);
		}

		p.updateStatusBasedOnStock();
		productRepo.save(p);

		// insert lines (ensure product persisted)
		upsertMaterials(p, req.getMaterials(), true);
		return toResponse(productRepo.findById(p.getMaSP()).get());
	}

	@Override
	@Transactional
	public ProductDetailResponse update(Integer maSP, ProductRequest req, MultipartFile file) {
		Product p = productRepo.findById(maSP).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		mapUpsert(p, req);

		if (file != null && !file.isEmpty()) {
			if (p.getHinhAnh() != null) {
				try {
					upload.deleteFile(p.getHinhAnh());
				} catch (Exception ex) {
				}
			}
			String saved = saveImage(file);
			p.setHinhAnh(saved);
		}

		p.updateStatusBasedOnStock();

		// replace lines
		pmRepo.deleteByMaSP_MaSP(maSP);
		upsertMaterials(p, req.getMaterials(), false);

		// persist changes
		productRepo.save(p);

		return toResponse(p);
	}

	@Override
	@Transactional
	public void delete(Integer maSP) {
		pmRepo.deleteByMaSP_MaSP(maSP);
		productRepo.deleteById(maSP);
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
		Optional<Product> opt = productRepo.findById(id);
		if (opt.isPresent()) {
			Product p = opt.get();
			if (p.getLuotXem() == null)
				p.setLuotXem(0);
			p.setLuotXem(p.getLuotXem() + 1);
			productRepo.save(p);
		}
	}

	@Override
	public Product saveProduct(Product product) {
		product.updateStatusBasedOnStock();
		return productRepo.save(product);
	}

	@Override
	public List<Product> searchByName(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return productRepo.findAll();
		}
		String k = keyword.toLowerCase(Locale.ROOT);
		return productRepo.findAll().stream()
				.filter(p -> p.getTenSP() != null && p.getTenSP().toLowerCase(Locale.ROOT).contains(k))
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> getTopViewed(int limit) {
		return productRepo.findAll().stream().sorted((a, b) -> {
			Integer va = a.getLuotXem() == null ? 0 : a.getLuotXem();
			Integer vb = b.getLuotXem() == null ? 0 : b.getLuotXem();
			return vb.compareTo(va);
		}).limit(limit).collect(Collectors.toList());
	}


	private void mapUpsert(Product p, ProductRequest req) {
		p.setTenSP(req.getTenSP());
		p.setDonViTinh(req.getDonViTinh());
		p.setGia(req.getGia());
		p.setSoLuongTon(Optional.ofNullable(req.getSoLuongTon()).orElse(0));
		p.setMoTa(req.getMoTa());
		if (req.getHinhAnh() != null)
			p.setHinhAnh(req.getHinhAnh()); 
		Category dm = categoryRepo.findById(req.getDanhMucId())
				.orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
		p.setDanhMuc(dm);
		if (p.getTrangThai() == null)
			p.setTrangThai(ProductStatus.DANG_BAN.getValue());
	}

	private void upsertMaterials(Product p, List<ProductMaterialLineRequest> lines, boolean needPersistProduct) {
		if (lines == null || lines.isEmpty())
			return;

		if (needPersistProduct && p.getMaSP() == null) {
			productRepo.saveAndFlush(p);
		}

		for (ProductMaterialLineRequest line : lines) {
			Material m = materialRepo.findById(line.getMaNL())
					.orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại: " + line.getMaNL()));

			Integer qty = line.getSoLuongCan();
			if (qty == null || qty <= 0)
				qty = 1;

			ProductMaterial pm = new ProductMaterial();
			ProductMaterialId id = new ProductMaterialId();
			id.setMaSP(p.getMaSP());
			id.setMaNL(m.getMaNL());

			pm.setId(id);
			pm.setMaSP(p);
			pm.setMaNL(m);
			pm.setSoLuongCan(qty);

			pmRepo.save(pm);
		}
	}

	private String saveImage(MultipartFile file) {
		try {
			// lưu vào thư mục con "products"
			return upload.saveFile(file, "products");
		} catch (Exception e) {
			throw new RuntimeException("Lưu ảnh thất bại: " + e.getMessage());
		}
	}

	private ProductDetailResponse toResponse(Product p) {
		ProductDetailResponse dto = new ProductDetailResponse();
		dto.setMaSP(p.getMaSP());
		dto.setTenSP(p.getTenSP());
		dto.setDonViTinh(p.getDonViTinh());
		dto.setGia(p.getGia());
		dto.setSoLuongTon(p.getSoLuongTon());
		dto.setMoTa(p.getMoTa());
		dto.setHinhAnh(p.getHinhAnh());
		dto.setTrangThai(p.getTrangThai());
		dto.setMaDM(p.getDanhMuc() != null ? p.getDanhMuc().getMaDM() : null);

		List<ProductDetailResponse.MaterialLine> lines = p
				.getProductMaterials().stream().map(pm -> new ProductDetailResponse.MaterialLine(pm.getMaNL().getMaNL(),
						pm.getMaNL().getTenNL(), pm.getMaNL().getDonViTinh(), pm.getSoLuongCan()))
				.collect(Collectors.toList());
		dto.setMaterials(lines);
		return dto;
	}
}
