package fourpetals.com.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import fourpetals.com.entity.Order;
import fourpetals.com.enums.ShippingFee;
import fourpetals.com.service.ShippingService;

@Service
public class ShippingServiceImpl implements ShippingService {

	@Override
	public BigDecimal getFee(ShippingFee type) {
		switch (type) {
		case NOI_THANH:
			return BigDecimal.valueOf(30000);
		case NGOAI_THANH:
			return BigDecimal.valueOf(50000);
		default:
			return BigDecimal.ZERO;
		}
	}

	@Override
	public BigDecimal calculateTotal(Order order) {
		BigDecimal tongSP = order.getChiTietDonHang().stream()
				.map(d -> d.getGiaBan().multiply(BigDecimal.valueOf(d.getSoLuong())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		ShippingFee type = ShippingFee.NOI_THANH; // tạm thời gán cứng
		BigDecimal shippingFee = getFee(type);
		order.setPhiVanChuyen(shippingFee);

		BigDecimal tongTien = tongSP.add(shippingFee);
		order.setTongTien(tongTien);
		return tongTien;
	}

}
