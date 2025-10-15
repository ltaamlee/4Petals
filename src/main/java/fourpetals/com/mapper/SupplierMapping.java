package fourpetals.com.mapper;

import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Supplier;
import fourpetals.com.entity.SupplierMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SupplierMapping {

	private SupplierMapping() {
	}

	public static SupplierResponse toSupplierResponse(Supplier supplier) {
		if (supplier == null)
			return null;

		SupplierResponse dto = new SupplierResponse();
		dto.setMaNCC(supplier.getMaNCC());
		dto.setTenNCC(supplier.getTenNCC());
		dto.setDiaChi(supplier.getDiaChi());
		dto.setSdt(supplier.getSdt());
		dto.setEmail(supplier.getEmail());
		dto.setCreatedAt(supplier.getCreatedAt());
		dto.setUpdatedAt(supplier.getUpdatedAt());

		// Không map nhaCungCapNguyenLieu => tránh LazyException
		dto.setNhaCungCapNguyenLieu(new ArrayList<>());

		return dto;
	}

	public static SupplierResponse toSupplierResponseDetail(Supplier supplier) {
		if (supplier == null)
			return null;

		SupplierResponse dto = new SupplierResponse();
		dto.setMaNCC(supplier.getMaNCC());
		dto.setTenNCC(supplier.getTenNCC());
		dto.setDiaChi(supplier.getDiaChi());
		dto.setSdt(supplier.getSdt());
		dto.setEmail(supplier.getEmail());
		dto.setCreatedAt(supplier.getCreatedAt());
		dto.setUpdatedAt(supplier.getUpdatedAt());

		List<SupplierMaterial> list = supplier.getNhaCungCapNguyenLieu();
		if (list != null && !list.isEmpty()) {
			// Id
			dto.setNhaCungCapNguyenLieu(
					list.stream().map(sm -> sm.getNguyenLieu().getMaNL()).collect(Collectors.toList()));

			// Tên nguyên liệu
			dto.setNhaCungCapNguyenLieuNames(
					list.stream().map(sm -> sm.getNguyenLieu().getTenNL()).collect(Collectors.toList()));
		}

		return dto;
	}

}
