
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
	    
	    Integer maNVDangNhap = null; // Biến lưu Mã NV
	    
	    if (userDetails != null) {
	        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
	        
	        if (userOpt.isPresent()) {
	            User user = userOpt.get();
	            model.addAttribute("user", user);
	            
	            // Lấy MaNV từ đối tượng User đang đăng nhập (Điều chỉnh theo cấu trúc Entity của bạn)
	            if (user.getNhanVien() != null) {
	                // Ví dụ: user.getEmployee() trả về đối tượng Employee, và getMaNV() lấy Mã NV
	                maNVDangNhap = user.getNhanVien().getMaNV(); 
	            }
	        }
	    }

	    List<Order> listOrders = List.of(); 
	    
	    if (maNVDangNhap != null) {
	        // 💡 SỬA ĐỔI: Gọi phương thức mới, chỉ lọc theo MaNV
	        listOrders = orderRepository.findAllOrdersByShipperMaNV(maNVDangNhap); 
	    }

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

		return result;
	}
	
	@Transactional
	@PostMapping("/updateOrders")
	public String capNhatTatCaDonHang(@RequestParam Map<String, String> paramMap,
	        RedirectAttributes redirectAttributes) {

	    List<String> thongBaoLoi = new ArrayList<>();
	    
	    // Sử dụng biến boolean để kiểm tra xem có bất kỳ đơn hàng nào được cập nhật thành công không
	    boolean coDonHangDuocCapNhat = false; 

	    for (String key : paramMap.keySet()) {
	        if (key.startsWith("status-")) {
	            try {
	                String maDHStr = key.replace("status-", "");
	                Integer maDH = Integer.valueOf(maDHStr);

	                String trangThaiMoiStr = paramMap.get(key);
	                String lyDo = paramMap.get("note-" + maDHStr);

	                OrderStatus trangThaiMoi = OrderStatus.valueOf(trangThaiMoiStr.toUpperCase());

	                // 1. KHÔI PHỤC LOGIC KIỂM TRA BẮT BUỘC NHẬP TẠI SERVER
	                if (OrderStatus.HUY.equals(trangThaiMoi) && (lyDo == null || lyDo.trim().isEmpty())) {
	                    // Nếu lỗi: KHÔNG LƯU và THÊM THÔNG BÁO LỖI VÀO DANH SÁCH
	                    thongBaoLoi.add("❌ Đơn hàng " + maDH + " thất bại nhưng chưa có ghi chú.");
	                } else {
	                    // Nếu hợp lệ: THỰC HIỆN LƯU
	                    Optional<Order> orderOpt = orderRepository.findById(maDH);

	                    if (orderOpt.isPresent()) {
	                        Order order = orderOpt.get();
	                        order.setTrangThai(trangThaiMoi);
	                        order.setLyDo(lyDo);
	                        orderRepository.save(order);
	                        coDonHangDuocCapNhat = true; // Đánh dấu là có cập nhật
	                        // ❌ KHÔNG THÊM THÔNG BÁO THÀNH CÔNG CỤ THỂ VÀO LIST
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
	    
	    // 2. CHỈ HIỂN THỊ THÔNG BÁO CHUNG SAU KHI VÒNG LẶP KẾT THÚC
	    if (coDonHangDuocCapNhat && thongBaoLoi.isEmpty()) {
	        redirectAttributes.addFlashAttribute("successMessages", List.of("✅ Đã lưu thành công các thay đổi."));
	    } else if (coDonHangDuocCapNhat && !thongBaoLoi.isEmpty()) {
	        redirectAttributes.addFlashAttribute("successMessages", List.of("✅ Một số đơn hàng đã được cập nhật."));
	    }

	    if (!thongBaoLoi.isEmpty()) {
	        // Chỉ hiển thị lỗi cho các đơn hàng vi phạm
	        redirectAttributes.addFlashAttribute("errorMessages", thongBaoLoi);
	    }

	    return "redirect:/shipper/process";
	}
}