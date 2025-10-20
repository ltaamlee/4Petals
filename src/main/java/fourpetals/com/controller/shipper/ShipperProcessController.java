package fourpetals.com.controller.shipper;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.OrderRepository;

@Controller
@RequestMapping("/shipper")
public class ShipperProcessController {

	@Autowired
	private OrderRepository orderRepository;

	// HIỂN THỊ DANH SÁCH ĐƠN HÀNG ĐANG XỬ LÝ
	@GetMapping("/process")
	public String hienThiDanhSachDonHangDangXuLy(Model model) {

		List<Order> listOrders = orderRepository.findAllDeliveringOrders();

		model.addAttribute("listOrders", listOrders);
		return "shipper/process";
	}

	// ---

	// ======= 2️⃣ LƯU CẬP NHẬT TRẠNG THÁI & GHI CHÚ CHO TẤT CẢ (Cập nhật vào DB)
	// =======
	@Transactional
	@PostMapping("/updateOrders")
	public String capNhatTatCaDonHang(@RequestParam Map<String, String> paramMap,
			RedirectAttributes redirectAttributes) { // 👈 SỬ DỤNG RedirectAttributes

		List<String> thongBaoLoi = new ArrayList<>();
		List<String> thongBaoThanhCong = new ArrayList<>();

		// ... (Logic xử lý vòng lặp và cập nhật DB - GIỮ NGUYÊN) ...
		// Tôi chỉ thay thế Model bằng RedirectAttributes để truyền thông báo

		for (String key : paramMap.keySet()) {
			if (key.startsWith("status-")) {
				try {
					String maDHStr = key.replace("status-", "");
					Integer maDH = Integer.valueOf(maDHStr);

					String trangThaiMoiStr = paramMap.get(key);
					String ghiChu = paramMap.get("note-" + maDHStr);

					OrderStatus trangThaiMoi = OrderStatus.valueOf(trangThaiMoiStr.toUpperCase());

					if (OrderStatus.HUY.equals(trangThaiMoi) && (ghiChu == null || ghiChu.trim().isEmpty())) {
						thongBaoLoi.add("❌ Đơn hàng " + maDH + " thất bại nhưng chưa có ghi chú.");
					} else {
						Optional<Order> orderOpt = orderRepository.findById(maDH);

						if (orderOpt.isPresent()) {
							Order order = orderOpt.get();
							order.setTrangThai(trangThaiMoi);
							order.setGhiChu(ghiChu);
							orderRepository.save(order);
							thongBaoThanhCong.add("✅ Đơn hàng " + maDH + " được cập nhật thành công!");
						} else {
							thongBaoLoi.add("❌ Không tìm thấy đơn hàng có mã " + maDH + " để cập nhật.");
						}
					}
				} catch (Exception e) {
					thongBaoLoi.add("❌ Lỗi hệ thống khi cập nhật đơn hàng.");
					e.printStackTrace();
				}
			}
		}

		// 1. Thêm thông báo vào RedirectAttributes (sẽ tồn tại trong request tiếp theo)
		if (!thongBaoThanhCong.isEmpty()) {
			redirectAttributes.addFlashAttribute("successMessages", thongBaoThanhCong);
		}
		if (!thongBaoLoi.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessages", thongBaoLoi);
		}

		// 2. Chuyển hướng về phương thức GET để tải lại danh sách
		return "redirect:/shipper/process";
	}
}