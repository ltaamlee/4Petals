package fourpetals.com.controller.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.dto.response.supplier.SupplierResponse;
import fourpetals.com.entity.Supplier;
import fourpetals.com.mapper.SupplierMapping;
import fourpetals.com.service.SupplierService;

@RestController
@RequestMapping("/api/inventory/suppliers")
public class InventorySupplierController {
	@Autowired
	private SupplierService supplierService;

	@GetMapping
	public Page<SupplierResponse> getSuppliers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) Integer materialId) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("maNCC").ascending());
		Page<Supplier> suppliersPage;

		if (materialId != null && materialId > 0) {
			suppliersPage = (keyword == null || keyword.isBlank())
					? supplierService.findSuppliersByMaterial(materialId, pageable)
					: supplierService.searchSuppliersByMaterial(materialId, keyword, pageable);
		} else {
			suppliersPage = (keyword == null || keyword.isBlank()) ? supplierService.findAll(pageable)
					: supplierService.search(keyword, pageable);
		}
        
		//VÍ DỤ NHƯ findSuppliersByMaterial LÀ GỌI TỪ SERVICE LÊN
		
		return suppliersPage.map(SupplierMapping::toSupplierResponse);
	}
}
