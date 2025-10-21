package fourpetals.com.service.impl;

import fourpetals.com.dto.request.customer.MomoCreateRequestDto;
import fourpetals.com.dto.response.customers.*;
import fourpetals.com.entity.Customer;
import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;
import fourpetals.com.enums.PaymentStatus;
import fourpetals.com.repository.OrderRepository;
import fourpetals.com.service.MomoService;
import fourpetals.com.utils.MomoSignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoServiceImpl implements MomoService {

	@Value("${momo.partnerCode}")
	private String partnerCode;
	@Value("${momo.accessKey}")
	private String accessKey;
	@Value("${momo.secretKey}")
	private String secretKey;
	@Value("${momo.requestUrl}")
	private String requestUrl;
	@Value("${momo.notifyUrl}")
	private String notifyUrl;
	@Value("${momo.returnUrl}")
	private String returnUrl;

	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	public MomoServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// ✅ Dùng cho trường hợp hiển thị QR trực tiếp (chưa lưu đơn)
	@Override
	public MomoPaymentResponse createQuickPayment(BigDecimal amount, String orderInfo) {
		String orderId = UUID.randomUUID().toString();
		String requestId = UUID.randomUUID().toString();

		String rawSignature = String.format(
				"accessKey=%s&amount=%d&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=captureWallet",
				accessKey, amount.intValue(), notifyUrl, orderId, orderInfo, partnerCode, returnUrl, requestId);
		String signature = MomoSignatureUtil.hmacSHA256(rawSignature, secretKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("partnerCode", partnerCode);
		requestBody.put("accessKey", accessKey);
		requestBody.put("requestId", requestId);
		requestBody.put("amount", amount.intValue());
		requestBody.put("orderId", orderId);
		requestBody.put("orderInfo", orderInfo);
		requestBody.put("redirectUrl", returnUrl);
		requestBody.put("ipnUrl", notifyUrl);
		requestBody.put("extraData", "");
		requestBody.put("requestType", "captureWallet");
		requestBody.put("signature", signature);
		requestBody.put("lang", "vi");

		ResponseEntity<Map> response = restTemplate.postForEntity(requestUrl, requestBody, Map.class);
		Map<?, ?> body = response.getBody();
		System.out.println("✅ Momo trả về: " + body);

		MomoPaymentResponse res = new MomoPaymentResponse();
		res.setPayUrl((String) body.get("payUrl"));
		res.setQrCodeUrl((String) body.get("qrCodeUrl"));
		System.out.println("✅ Đã tạo QR MoMo, orderId=" + orderId);
		return res;
	}

	// ✅ Tạo đơn hàng (dùng cho COD hoặc callback MoMo)
	@Override
	public Order createOrder(Customer customer, String tenNguoiNhan, String sdt, String diaChi, String ghiChu) {
		Order order = new Order();
		order.setKhachHang(customer);
		order.setDiaChiGiao(diaChi);
		order.setSdtNguoiNhan(sdt);
		order.setGhiChu(ghiChu);
		order.setNgayDat(LocalDateTime.now());
		order.setTrangThai(OrderStatus.CHO_XU_LY);
		order.setTrangThaiThanhToan(PaymentStatus.CHUA_THANH_TOAN);
		return orderRepository.save(order);
	}

	// ✅ Dùng khi đã có Order (từ giỏ hàng) và muốn tạo thanh toán MoMo thật
	@Override
	public MomoCreateResponseDto createPayment(Order order) {
		String requestId = UUID.randomUUID().toString();
		String orderId = "ORDER-" + order.getMaDH();

		// 1. SỬA LỖI: Lấy amount dưới dạng SỐ (long), không phải String
		long amount = order.getTongTien().longValue();

		String orderInfo = "Thanh toán đơn hàng #" + order.getMaDH();

		// 2. SỬA LỖI: Dùng %d (cho số) thay vì %s (cho string)
		String rawSignature = String.format(
				"accessKey=%s&amount=%d&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=captureWallet",
				accessKey, amount, notifyUrl, orderId, orderInfo, partnerCode, returnUrl, requestId);
		String signature = MomoSignatureUtil.hmacSHA256(rawSignature, secretKey);

		// 3. SỬA LỖI: Gửi request bằng Map<String, Object> giống như hàm
		// createQuickPayment
		// thay vì dùng MomoCreateRequestDto
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("partnerCode", partnerCode);
		requestBody.put("accessKey", accessKey);
		requestBody.put("requestId", requestId);
		requestBody.put("amount", amount); // Gửi đi dưới dạng SỐ
		requestBody.put("orderId", orderId);
		requestBody.put("orderInfo", orderInfo);
		requestBody.put("redirectUrl", returnUrl);
		requestBody.put("ipnUrl", notifyUrl);
		requestBody.put("extraData", "");
		requestBody.put("requestType", "captureWallet");
		requestBody.put("signature", signature);
		requestBody.put("lang", "vi");

		// 4. SỬA LỖI: Dùng postForEntity thay vì exchange
		ResponseEntity<MomoCreateResponseDto> resp = restTemplate.postForEntity(requestUrl, requestBody,
				MomoCreateResponseDto.class);

		System.out.println("⏰ QR MoMo cho đơn #" + order.getMaDH() + " có hiệu lực 10 phút.");
		return resp.getBody();
	}
	
	@Override
	public boolean handleMomoReturn(String orderId, int resultCode) {
	    if (resultCode == 0) {
	        // Thanh toán thành công (resultCode = 0)
	        // Chúng ta không làm gì ở đây, vì chúng ta chờ IPN (handleCallback)
	        // để cập nhật trạng thái, chỉ báo cho Controller biết là thành công.
	        return true; 
	    }

	    // Thanh toán thất bại hoặc bị HỦY (resultCode != 0)
	    try {
	        // 1. Parse maDH từ orderId (logic y hệt handleCallback)
	        // (logic cũ của bạn)
	        String maDHString = orderId.replace("ORDER-", "");
	        if (maDHString.contains("-T")) { // Xử lý timestamp nếu có
	             maDHString = maDHString.split("-T")[0]; 
	        }
	        int maDH = Integer.parseInt(maDHString);
	        
	        // 2. Tìm đơn hàng
	        Order order = orderRepository.findById(maDH).orElse(null);
	        
	        // 3. Nếu tìm thấy và đơn hàng vẫn đang chờ (CHUA_THANH_TOAN)
	        if (order != null && order.getTrangThaiThanhToan() == PaymentStatus.CHUA_THANH_TOAN) {
	            
	            // 4. XÓA ĐƠN HÀNG 
	            orderRepository.delete(order);
	            System.out.println("🚮 Đã xóa đơn hàng #" + maDH + " do người dùng hủy thanh toán.");
	        }
	        
	        // 5. Báo cho controller biết là thất bại
	        return false; 
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // Có lỗi xảy ra
	    }
	}

	// ✅ Callback từ MoMo xác nhận thanh toán
	@Override
	public void handleCallback(String orderId, int resultCode) {
		try {
			int maDH = Integer.parseInt(orderId.replace("ORDER-", ""));
			Order order = orderRepository.findById(maDH).orElse(null);
			if (order == null)
				return;

			if (resultCode == 0) {
				order.setTrangThaiThanhToan(PaymentStatus.DA_THANH_TOAN);
			} else {
				order.setTrangThaiThanhToan(PaymentStatus.THAT_BAI);
			}

			orderRepository.save(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
