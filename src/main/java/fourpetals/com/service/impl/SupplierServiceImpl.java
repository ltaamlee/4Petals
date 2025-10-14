package fourpetals.com.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.dto.request.supplier.SupplierRequest;
import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;
import fourpetals.com.entity.SupplierMaterialId;
import fourpetals.com.mapper.SupplierMapping;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierMaterialRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.service.SupplierService;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	private final SupplierRepository supplierRepository;
	private final MaterialRepository materialRepository;
	private final SupplierMaterialRepository supplierMaterialRepository;

	public SupplierServiceImpl(SupplierRepository supplierRepository, MaterialRepository materialRepository,
			SupplierMaterialRepository supplierMaterialRepository) {
		this.supplierRepository = supplierRepository;
		this.materialRepository = materialRepository;
		this.supplierMaterialRepository = supplierMaterialRepository;
	}

	// ----------------- CRUD -----------------
	@Override
	@Transactional
	public Supplier create(SupplierRequest request) {
		// 1. Tạo supplier mới
		Supplier supplier = new Supplier();
		supplier.setTenNCC(request.getTenNCC());
		supplier.setDiaChi(request.getDiaChi());
		supplier.setSdt(request.getSdt());
		supplier.setEmail(request.getEmail());

		Supplier savedSupplier = supplierRepository.saveAndFlush(supplier); // flush ngay

		// 2. Lưu SupplierMaterial nếu có
		if (request.getNhaCungCapNguyenLieu() != null && !request.getNhaCungCapNguyenLieu().isEmpty()) {
			for (Integer maNL : request.getNhaCungCapNguyenLieu()) {
				Material material = materialRepository.findById(maNL)
						.orElseThrow(() -> new IllegalArgumentException("Nguyên liệu không tồn tại: " + maNL));

				SupplierMaterial sm = new SupplierMaterial();
				sm.setNhaCungCap(savedSupplier);
				sm.setNguyenLieu(material);
				sm.setId(new SupplierMaterialId(savedSupplier.getMaNCC(), material.getMaNL()));

				supplierMaterialRepository.save(sm);
			}

			supplierMaterialRepository.flush(); // flush các liên kết xuống DB
		}

		// 3. Load lại Supplier cùng nguyên liệu
		return supplierRepository.findByIdWithMaterials(savedSupplier.getMaNCC())
				.orElseThrow(() -> new RuntimeException("Không lấy được nguyên liệu"));
	}

	@Override
	@Transactional
	public Supplier update(SupplierRequest request) {
		// 1. Lấy nhà cung cấp từ DB
		Supplier supplier = supplierRepository.findById(request.getMaNCC())
				.orElseThrow(() -> new IllegalArgumentException("Nhà cung cấp không tồn tại: " + request.getMaNCC()));

		// 2. Cập nhật thông tin cơ bản
		supplier.setTenNCC(request.getTenNCC());
		supplier.setDiaChi(request.getDiaChi());
		supplier.setSdt(request.getSdt());
		supplier.setEmail(request.getEmail());
		Supplier updated = supplierRepository.save(supplier);

		// 3. Cập nhật nguyên liệu nếu có
		List<Integer> materialIds = request.getNhaCungCapNguyenLieu();
		if (materialIds != null) {
			// Xóa liên kết cũ
			supplierMaterialRepository.deleteByNhaCungCapMaNCC(updated.getMaNCC());

			// Tạo liên kết mới
			List<SupplierMaterial> newLinks = new ArrayList<>();
			for (Integer maNL : materialIds) {
				Material material = materialRepository.findById(maNL)
						.orElseThrow(() -> new IllegalArgumentException("Nguyên liệu không tồn tại: " + maNL));
				SupplierMaterial sm = new SupplierMaterial();
				sm.setNhaCungCap(updated);
				sm.setNguyenLieu(material);
				newLinks.add(sm);
			}
			supplierMaterialRepository.saveAll(newLinks); // save batch
		}

		return updated;
	}

	@Override
	public void delete(Integer maNCC) {
		Supplier supplier = supplierRepository.findById(maNCC)
				.orElseThrow(() -> new IllegalArgumentException("Nhà cung cấp không tồn tại: " + maNCC));

		// Xóa các SupplierMaterial liên quan
		if (supplier.getNhaCungCapNguyenLieu() != null && !supplier.getNhaCungCapNguyenLieu().isEmpty()) {
			supplierMaterialRepository.deleteAll(supplier.getNhaCungCapNguyenLieu());
		}

		// Xóa supplier
		supplierRepository.delete(supplier);
	}

	// ----------------- Tìm kiếm / danh sách -----------------
	@Override
	@Transactional(readOnly = true)
	public Page<Supplier> findAll(Pageable pageable) {
		return supplierRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Supplier> search(String keyword, Pageable pageable) {
		return supplierRepository.searchSuppliers(keyword, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Supplier> findSuppliersByMaterial(Integer maNL, Pageable pageable) {
		return supplierRepository.findSuppliersByMaterial(maNL, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Supplier> searchSuppliersByMaterial(Integer maNL, String keyword, Pageable pageable) {
		return supplierRepository.searchSuppliersByMaterial(maNL, keyword, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Supplier> findById(Integer maNCC) {
		return supplierRepository.findById(maNCC);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Supplier> findByTenNCC(String tenNCC) {
		return supplierRepository.findByTenNCC(tenNCC);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Supplier> findByIdWithMaterials(Integer maNCC) {
		return supplierRepository.findByIdWithMaterials(maNCC);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Supplier> findByEmail(String email) {
		return supplierRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Supplier> findBySdt(String sdt) {
		return supplierRepository.findBySdt(sdt);
	}

	@Override
	@Transactional(readOnly = true)
	public long countTotalSuppliers() {
		return supplierRepository.countTotalSuppliers();
	}

	// ----------------- Nguyên liệu -----------------

	@Override
	@Transactional(readOnly = true)
	public SupplierResponse getSupplierDetail(Integer id) {
		// Lấy supplier kèm nguyên liệu
		Supplier supplier = supplierRepository.findByIdWithMaterials(id)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà cung cấp với id " + id));

		// Map sang DTO
		SupplierResponse dto = SupplierMapping.toSupplierResponseDetail(supplier);

		// Lấy tên nguyên liệu
		if (supplier.getNhaCungCapNguyenLieu() != null) {
			dto.setNhaCungCapNguyenLieuNames(
					supplier.getNhaCungCapNguyenLieu().stream().map(sm -> sm.getNguyenLieu().getTenNL()).toList());
		}

		return dto;
	}

}
