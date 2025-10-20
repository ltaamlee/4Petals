package fourpetals.com.service;

import java.math.BigDecimal;

import fourpetals.com.entity.Order;
import fourpetals.com.enums.ShippingFee;

public interface ShippingService {
	
	BigDecimal getFee(ShippingFee type); // tính phí vận chuyển
	BigDecimal calculateTotal(Order order); // tính tổng tiền: sản phẩm + phí + khuyến mãi
}
