package fourpetals.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.dto.request.products.ProductMaterialLineRequest;
import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.dto.response.promotions.PromotionResponse;
import fourpetals.com.entity.Category;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.ProductMaterial;
import fourpetals.com.entity.ProductMaterialId;
import fourpetals.com.enums.CustomerRank;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.model.ProductRowVM;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.OrderDetailRepository;
import fourpetals.com.repository.ProductMaterialRepository;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.ProductBannerService;
import fourpetals.com.service.ProductService;
import fourpetals.com.service.PromotionService;
import fourpetals.com.utils.Upload;
import org.springframework.data.domain.Pageable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

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

	@Autowired
	private OrderDetailRepository orderDetailRepo; // để tính sold

	@Autowired
	private ProductBannerService productBannerService;

	@Autowired
	private PromotionService promotionService;

	@Override
	@Transactional(readOnly = true)
	public List<ProductRowVM> searchNoPaging(String kw, Integer status, Integer categoryId) {
		var list = productRepo.searchNoPaging(kw == null ? "" : kw, status, categoryId);
		var ids = list.stream().map(Product::getMaSP).toList();
		var soldMap = orderDetailRepo.sumSoldByProductIds(ids); // Map<Integer, Long>

		return list.stream().map(p -> new ProductRowVM(p.getMaSP(), p.getTenSP(), p.getHinhAnh(), p.getGia(),
				p.getTrangThai(), p.getTrangThaiText(), soldMap.getOrDefault(p.getMaSP(), 0L))).toList();
	}

	@Override
	public Integer create(ProductRequest req) {
		Product p = new Product();
		p.setTenSP(req.getTenSP());
		p.setGia(req.getGia());
		p.setTrangThai(req.getTrangThai() == null ? ProductStatus.DANG_BAN.getValue() : req.getTrangThai());
		mapUpsert(p, req);
		String baseDesc = req.getMoTa() == null ? "" : req.getMoTa();
		String materialDesc = buildMaterialDescription(req.getMaterials());
		if (!materialDesc.isEmpty()) {
			baseDesc += "\nNguyên liệu: " + materialDesc;
		}
		p.setMoTa(baseDesc);
		productRepo.saveAndFlush(p);
		upsertMaterials(p, req.getMaterials(), false);
		return p.getMaSP();
	}

	@Override
	public void update(Integer id, ProductRequest req) {
		Product p = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		if (req.getTenSP() != null)
			p.setTenSP(req.getTenSP());
		if (req.getGia() != null)
			p.setGia(req.getGia());
		if (req.getTrangThai() != null)
			p.setTrangThai(req.getTrangThai());
		mapUpsert(p, req);

		String baseDesc = req.getMoTa() == null ? "" : req.getMoTa();
		String materialDesc = buildMaterialDescription(req.getMaterials());
		if (!materialDesc.isEmpty()) {
			baseDesc += "\nNguyên liệu: " + materialDesc;
		}
		p.setMoTa(baseDesc);

		productRepo.saveAndFlush(p);
		pmRepo.deleteByMaSP_MaSP(id);
		upsertMaterials(p, req.getMaterials(), false);
	}

	@Override
	public Optional<ProductDetailResponse> getDetail(Integer id) {
		return productRepo.findById(id).map(p -> toResponse(p, null));
	}

	@Override
	public ProductDetailResponse findById(Integer maSP) {
		Product p = productRepo.findById(maSP).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		// ensure materials fetched if lazy
		if (p.getProductMaterials() != null)
			p.getProductMaterials().size();
		return toResponse(p, null);
	}

	// Lấy nguyên liệu ghi vào phần mô tả
	private String buildMaterialDescription(List<ProductMaterialLineRequest> lines) {
		if (lines == null || lines.isEmpty())
			return "";
		return lines.stream().map(line -> {
			Material m = materialRepo.findById(line.getMaNL())
					.orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại: " + line.getMaNL()));
			Integer qty = line.getSoLuongCan() == null ? 1 : line.getSoLuongCan();
			return m.getTenNL() + " (" + qty + " " + m.getDonViTinh() + ")";
		}).collect(Collectors.joining(", "));
	}

	@Override
	@Transactional
	public ProductDetailResponse create(ProductRequest req, MultipartFile file) {
		Product p = new Product();
		mapUpsert(p, req); // set các field cơ bản, trừ moTa

		// Upload ảnh nếu có
		if (file != null && !file.isEmpty()) {
			String saved = saveImage(file, "products");
			p.setHinhAnh(saved);
		}

		p.updateStatusBasedOnStock();
		productRepo.save(p); // lưu để có ID

		// Lưu nguyên liệu
		upsertMaterials(p, req.getMaterials(), true);

		// Nối mô tả + nguyên liệu
		String baseDesc = req.getMoTa() == null ? "" : req.getMoTa();
		String materialDesc = buildMaterialDescription(req.getMaterials());
		if (!materialDesc.isEmpty()) {
			baseDesc += "\nNguyên liệu: " + materialDesc;
		}
		p.setMoTa(baseDesc);

		productRepo.saveAndFlush(p); // lưu vào DB

		return toResponse(p, null);
	}

	@Override
	@Transactional
	public ProductDetailResponse update(Integer maSP, ProductRequest req, MultipartFile file) {
		Product p = productRepo.findById(maSP).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		mapUpsert(p, req); // set các field cơ bản, trừ moTa

		if (file != null && !file.isEmpty()) {
			if (p.getHinhAnh() != null)
				try {
					upload.deleteFile(p.getHinhAnh());
				} catch (Exception ignore) {
				}
			String saved = saveImage(file, "products");
			p.setHinhAnh(saved);
		}

		p.updateStatusBasedOnStock();

		// Xóa và lưu lại nguyên liệu
		pmRepo.deleteByMaSP_MaSP(maSP);
		upsertMaterials(p, req.getMaterials(), false);

		// Nối mô tả + nguyên liệu
		String baseDesc = req.getMoTa() == null ? "" : req.getMoTa();
		String materialDesc = buildMaterialDescription(req.getMaterials());
		if (!materialDesc.isEmpty()) {
			baseDesc += "\nNguyên liệu: " + materialDesc;
		}
		p.setMoTa(baseDesc);

		productRepo.saveAndFlush(p);

		return toResponse(p, null);
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
	@Transactional
	public void increaseViewCount(Integer productId) {
		productRepo.findById(productId).ifPresent(p -> {
			p.setLuotXem(p.getLuotXem() + 1);
			productRepo.save(p);
		});
	}

	@Override
	public Product saveProduct(Product product) {
		product.updateStatusBasedOnStock();
		return productRepo.save(product);
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public List<Product> searchAndFilter(String keyword, List<Integer> categoryIds) {
		if ((keyword == null || keyword.isBlank()) && (categoryIds == null || categoryIds.isEmpty())) {
			return productRepo.findAll();
		}

		if (keyword != null && !keyword.isBlank() && categoryIds != null && !categoryIds.isEmpty()) {
			return productRepo.findByTenSPContainingAndDanhMucIn(keyword, categoryIds);
		}

		if (keyword != null && !keyword.isBlank()) {
			return productRepo.findByTenSPContainingIgnoreCase(keyword);
		}

		return productRepo.findByDanhMucIn(categoryIds);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getTopViewed(int limit) {
		return productRepo.findAll().stream().sorted((a, b) -> {
			Integer va = a.getLuotXem() == null ? 0 : a.getLuotXem();
			Integer vb = b.getLuotXem() == null ? 0 : b.getLuotXem();
			return vb.compareTo(va);
		}).limit(limit).collect(Collectors.toList());
	}

	private void mapUpsert(Product p, ProductRequest req) {
		// Các field chung
		if (req.getDonViTinh() != null)
			p.setDonViTinh(req.getDonViTinh());
		if (req.getSoLuongTon() != null)
			p.setSoLuongTon(req.getSoLuongTon());
		if (req.getHinhAnh() != null)
			p.setHinhAnh(req.getHinhAnh());

		// Set danh mục (chỉ 1 lần)
		if (req.getDanhMucId() != null) {
			Category dm = categoryRepo.findById(req.getDanhMucId())
					.orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
			p.setDanhMuc(dm);
		}

		// Set trạng thái mặc định nếu chưa có
		if (p.getTrangThai() == null) {
			p.setTrangThai(ProductStatus.DANG_BAN.getValue());
		}
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

	@Value("${file.upload-dir}")
	private String uploadRoot;

	private String saveImage(MultipartFile file, String subDir) {
		try {
			// thư mục gốc lấy từ application.properties
			Path root = Paths.get(uploadRoot).normalize(); // E:/SOEN_Project/4Petals/uploads/
			String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
			// thay ký tự nguy hiểm, tùy ý:
			filename = filename.replaceAll("[\\s]+", "_");

			Path target = root.resolve(subDir).resolve(filename).normalize(); // .../uploads/products/xxx.jpg
			Files.createDirectories(target.getParent());
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

			// TRẢ VỀ ĐƯỜNG DẪN WEB (khớp WebMvcConfig ở trên)
			return "/uploads/" + subDir + "/" + filename;
		} catch (IOException e) {
			throw new RuntimeException("Lưu ảnh thất bại: " + e.getMessage(), e);
		}
	}

	@Override
	public void updateImage(Integer maSP, MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new RuntimeException("File ảnh trống");
		}
		Product p = productRepo.findById(maSP).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		// (tuỳ chọn) xóa ảnh cũ nếu bạn muốn
		// if (p.getHinhAnh() != null) try { upload.deleteFile(p.getHinhAnh()); } catch
		// (Exception ignore) {}

		String webPath = saveImage(file, "products");
		p.setHinhAnh(webPath);
		productRepo.save(p);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDetailResponse getDetailWithMaterials(Integer id) {
		Product p = productRepo.findByIdWithMaterials(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		// Ép load danh mục & nguyên liệu (vẫn trong transaction)
		if (p.getDanhMuc() != null)
			p.getDanhMuc().getMaDM();

		p.getProductMaterials().forEach(pm -> {
			if (pm.getMaNL() != null) {
				pm.getMaNL().getMaNL();
				pm.getMaNL().getTenNL();
			}
		});
		return toResponse(p, null);
	}


	public List<Product> getRelatedProducts(Integer categoryId, Integer currentProductId) {
	    Pageable limit5 = PageRequest.of(0, 5);
	    return productRepo.findTop5ByCategoryWithDetails(categoryId, currentProductId, limit5);
	}


	// KHUYẾN MÃI

	@Override
	public ProductDetailResponse toResponse(Product p, CustomerRank rank) {
		ProductDetailResponse dto = new ProductDetailResponse();
		dto.setMaSP(p.getMaSP());
		dto.setTenSP(p.getTenSP());
		dto.setDonViTinh(p.getDonViTinh());
		dto.setGia(p.getGia());
		dto.setSoLuongTon(p.getSoLuongTon());
		dto.setMoTa(p.getMoTa());
		dto.setHinhAnh(p.getHinhAnh());
		dto.setTrangThai(p.getTrangThai());
		dto.setTrangThaiText(p.getTrangThaiText());
		dto.setMaDM(p.getDanhMuc() != null ? p.getDanhMuc().getMaDM() : null);
		dto.setCustomerRank(rank);

		// --- Gán nguyên liệu ---
		if (p.getProductMaterials() != null) {
			List<ProductDetailResponse.MaterialLine> lines = p.getProductMaterials().stream()
					.map(pm -> new ProductDetailResponse.MaterialLine(pm.getMaNL().getMaNL(), pm.getMaNL().getTenNL(),
							pm.getMaNL().getDonViTinh(), pm.getSoLuongCan()))
					.collect(Collectors.toList());
			dto.setMaterials(lines);
		}

		// --- Gán banner khuyến mãi ---
		dto.setBannerKhuyenMai(promotionService.findActiveBannerForProduct(p.getMaSP()));
		String banner = dto.getBannerKhuyenMai();

		BigDecimal giaGoc = p.getGia() != null ? p.getGia() : BigDecimal.ZERO;
		dto.setGiaSauKhuyenMai(giaGoc); // mặc định = giá gốc
		dto.setGiamPhanTram(0);
		dto.setLoaiKhuyenMai(null);

		System.out.println("---------- DEBUG KHUYẾN MÃI ----------");
		System.out.println("Sản phẩm: " + p.getTenSP());
		System.out.println("Giá gốc: " + giaGoc);
		System.out.println("Banner: " + banner);
		System.out.println("Customer Rank: " + rank);

		if (banner != null && !banner.trim().isEmpty()) {
			// ✅ Loại giảm theo %
			if (banner.matches(".*\\d+(\\.\\d+)?%.*")) {
				dto.setLoaiKhuyenMai("PERCENT");
				try {
					Matcher m = Pattern.compile("(\\d+(\\.\\d+)?)%").matcher(banner);
					if (m.find()) {
						BigDecimal percent = new BigDecimal(m.group(1)); // 16.00
						BigDecimal discount = p.getGia().multiply(percent).divide(new BigDecimal("100"), 0,
								RoundingMode.HALF_UP);
						BigDecimal giaSauKM = p.getGia().subtract(discount);
						dto.setGiaSauKhuyenMai(giaSauKM);

						dto.setGiaSauKhuyenMai(giaSauKM);
						dto.setGiamPhanTram(percent.intValue());

						System.out.println("→ Loại: Giảm theo %, giảm " + percent + "%");
						System.out.println("→ Giá sau KM: " + giaSauKM);
					}
				} catch (Exception e) {
					dto.setGiaSauKhuyenMai(giaGoc);
				}

				// ✅ Loại giảm theo số tiền (₫)
			} else if (banner.matches(".*\\d+[₫đ].*")) {
				dto.setLoaiKhuyenMai("AMOUNT");
				try {
					Matcher m = Pattern.compile("(\\d+(?:\\.\\d+)*)[₫đ]").matcher(banner);
					if (m.find()) {
						String amountStr = m.group(1).replace(".", "");
						BigDecimal discount = new BigDecimal(amountStr);
						BigDecimal newPrice = giaGoc.subtract(discount).max(BigDecimal.ZERO);
						dto.setGiaSauKhuyenMai(newPrice);
					}
				} catch (Exception e) {
					dto.setGiaSauKhuyenMai(giaGoc);
				}

				// ✅ Còn lại là quà tặng / giảm khác
			} else {
				dto.setLoaiKhuyenMai("GIFT");
				dto.setGiaSauKhuyenMai(null);
			}
		} else {
			dto.setLoaiKhuyenMai(null);
		}
		Optional<PromotionResponse> promoOpt = promotionService.getActivePromotionForProduct(p.getMaSP(), rank);
		if (promoOpt.isPresent()) {
		    PromotionResponse promo = promoOpt.get();
		    dto.setBannerKhuyenMai(promo.getTenkm());
		    dto.setLoaiKhuyenMai(promo.getLoaiKm().name());
		    dto.setGiaSauKhuyenMai(promotionService.getDiscountedPrice(p.getGia(), promo));
		}
		if (rank == null) {
		    rank = CustomerRank.THUONG;
		}



		return dto;
	}

	@Override
	@Transactional
	public List<Product> findAllWithMaterials() {
		return productRepo.findAllWithMaterials();
	}

	@Override
	@Transactional
	public Optional<Product> findByIdWithMaterials(Integer id) {
		return productRepo.findByIdWithMaterials(id);
	}


	@Override
	public List<ProductDetailResponse> getTopPromotionalProducts(CustomerRank rank) {
	    Pageable top5 = PageRequest.of(0, 5);
	    List<Product> products = productRepo.findTop5PromotionalProducts(top5);
	    return products.stream()
	            .map(p -> toResponse(p, rank))
	            .toList();
	}

	@Override
	public List<ProductDetailResponse> getTopViewedProductsWithPromo(CustomerRank rank) {
	    List<Product> topProducts = productRepo.findTop10ViewedWithActivePromotion();
	    return topProducts.stream()
	            .map(p -> toResponse(p, rank))
	            .collect(Collectors.toList());
	}





}
