package fourpetals.com.controller.shipper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fourpetals.com.entity.Order;
import fourpetals.com.entity.User;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.security.CustomUserDetails;
import fourpetals.com.service.UserService;

@Controller
@RequestMapping("/shipper")
public class ShipperProcessController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserService userService;

	// HIỂN THỊ DANH SÁCH ĐƠN HÀNG ĐANG XỬ LÝ
	@GetMapping("/process")
	public String hienThiDanhSachDonHangDangXuLy(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		if (userDetails != null) {
			Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
			userOpt.ifPresent(user -> model.addAttribute("user", user));
		}
		// Sửa lại để chỉ lấy các đơn hàng đang được giao
		List<Order> listOrders = orderRepository.findAllDeliveringOrders();

		model.addAttribute("listOrders", listOrders);
		return "shipper/process";
	}

	// ---------------------------------------------------------------------------------
	// XEM CHI TIẾT ĐƠN HÀNG (ĐÃ BỔ SUNG)
	// ---------------------------------------------------------------------------------
	@Transactional
	@GetMapping("/{maDH}/details")
	@ResponseBody
	public Map<String, Object> getOrderDetails(@PathVariable("maDH") Integer maDH) {
		Map<String, Object> result = new HashMap<>();

		Order donHang = orderRepository.findById(maDH).orElse(null);
		if (donHang == null) {
			result.put("error", "Không tìm thấy đơn hàng");
			return result;
		}

		// Lấy thông tin chi tiết
		String tenKhachHang = donHang.getKhachHang() != null ? donHang.getKhachHang().getHoTen() : "Không có";
		String diaChi = donHang.getDiaChiGiao() != null ? donHang.getDiaChiGiao() : "Không có";
		String soDienThoai = donHang.getSdtNguoiNhan() != null ? donHang.getSdtNguoiNhan() : "Không có";
		String thanhTien = donHang.getTongTien() != null ? donHang.getTongTien().toPlainString() : "0";

		String phuongThucThanhToan = donHang.getPhuongThucThanhToan() != null
				? donHang.getPhuongThucThanhToan().toString()
				: "Chưa xác định";
		String ghiChu = donHang.getGhiChu() != null ? donHang.getGhiChu() : "Không có";

		// Xử lý danh sách sản phẩm
		String sanPham = donHang.getChiTietDonHang().stream()
				.map(ct -> ct.getSanPham().getTenSP() + " (x" + ct.getSoLuong() + ")")
				.collect(Collectors.joining(", "));

		// Đặt kết quả vào Map để trả về JSON
		result.put("tenKhachHang", tenKhachHang);
		result.put("diaChi", diaChi);
		result.put("soDienThoai", soDienThoai);
		result.put("sanPham", sanPham);
		result.put("thanhTien", thanhTien);
		result.put("phuongThucThanhToan", phuongThucThanhToan);
		result.put("ghiChu", ghiChu);

		return result;
	}
	// ---------------------------------------------------------------------------------
	// END XEM CHI TIẾT ĐƠN HÀNG
	// ---------------------------------------------------------------------------------

	@Transactional
	@PostMapping("/updateOrders")
	public String capNhatTatCaDonHang(@RequestParam Map<String, String> paramMap,
			RedirectAttributes redirectAttributes) {

		List<String> thongBaoLoi = new ArrayList<>();
		List<String> thongBaoThanhCong = new ArrayList<>();

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

		if (!thongBaoThanhCong.isEmpty()) {
			redirectAttributes.addFlashAttribute("successMessages", thongBaoThanhCong);
		}
		if (!thongBaoLoi.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessages", thongBaoLoi);
		}

		return "redirect:/shipper/process";
	}
}