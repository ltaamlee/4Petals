package fourpetals.com.controller.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import fourpetals.com.dto.controller.MaterialDetailDTO;
import fourpetals.com.dto.controller.OrderDetailDTO;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.OrderRepository;

@Controller
@RequestMapping("inventory/orders")
public class InventoryOrderController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private MaterialRepository materialRepository;

	@GetMapping
	public String listOrders(Model model) {
		List<Order> listOrders = orderRepository.findAllConfirmedOrders();
		model.addAttribute("listOrders", listOrders);

		return "inventory/orders";
	}

	// Lấy chi tiết đơn hàng 
	@Transactional
	@GetMapping("/{maDH}/details")
	@ResponseBody
	public List<OrderDetailDTO> getOrderDetails(@PathVariable Integer maDH) {
		return orderRepository.findById(maDH).map(order -> order.getChiTietDonHang().stream().map(ct -> {
			Integer soLuongSanPhamDat = ct.getSoLuong();

			List<MaterialDetailDTO> materialDetails = ct.getSanPham().getProductMaterials().stream().map(pm -> {
				Integer soLuongCan1SP = pm.getSoLuongCan();
				Integer tongSoLuongCan = soLuongCan1SP * soLuongSanPhamDat;
				return new MaterialDetailDTO(
						pm.getMaNL().getTenNL(), soLuongCan1SP,
						pm.getMaNL().getDonViTinh(), tongSoLuongCan);
			}).collect(Collectors.toList());
			return new OrderDetailDTO(ct.getSanPham().getTenSP(), ct.getSoLuong(), materialDetails);
		}).collect(Collectors.toList())).orElse(List.of());
	}

	@PostMapping("/{maDH}/confirm")
	@ResponseBody
	@Transactional
	public void confirmOrder(@PathVariable Integer maDH) { 
		Order order = orderRepository.findById(maDH)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có mã " + maDH));

		// TRỪ TỒN KHO NGUYÊN LIỆU 
		for (var detail : order.getChiTietDonHang()) {
			Integer soLuongSanPhamDat = detail.getSoLuong();

			for (var productMaterial : detail.getSanPham().getProductMaterials()) {

				var material = productMaterial.getMaNL();
				Integer soLuongCan1SP = productMaterial.getSoLuongCan();
				Integer tongSoLuongCanTru = soLuongCan1SP * soLuongSanPhamDat;

				Integer tonKhoHienTai = material.getSoLuongTon();

				if (tonKhoHienTai == null || tonKhoHienTai < tongSoLuongCanTru) {
					String errorMessage = "LỖI TỒN KHO: Nguyên liệu không đủ để đóng gói đơn hàng này";
					throw new RuntimeException(errorMessage);
				}
				material.setSoLuongTon(tonKhoHienTai - tongSoLuongCanTru);
				materialRepository.save(material);
			}
		}

		// CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG 
		order.setTrangThai(OrderStatus.DA_DONG_DON);
		orderRepository.save(order);
	}
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleRuntimeException(RuntimeException ex) {
		return ex.getMessage();
	}
}
