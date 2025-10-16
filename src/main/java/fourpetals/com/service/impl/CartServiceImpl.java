package fourpetals.com.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fourpetals.com.entity.Cart;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;
import fourpetals.com.repository.CartRepository;
import fourpetals.com.repository.ProductRepository;
import fourpetals.com.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepo;
    @Autowired private ProductRepository productRepo;

    @Override
    public List<Cart> getCartByUser(User user) {
        return cartRepo.findByNguoiDung(user);
    }
    
    @Override
    public void addToCart(User user, Integer productId, Integer quantity) {
        Product product = productRepo.findById(productId).orElseThrow();

        // Kiểm tra xem sp đã tồn tại trong giỏ chưa
        Cart item = cartRepo.findByNguoiDung_UserIdAndSanPham_MaSP(user.getUserId(), productId)
                .orElse(new Cart());

        item.setNguoiDung(user);
        item.setSanPham(product);
        item.setSoLuong(item.getSoLuong() == null ? quantity : item.getSoLuong() + quantity);
        item.setTongTien(product.getGia().multiply(new BigDecimal(item.getSoLuong())));

        cartRepo.save(item);
    }
    
    @Override
    public void updateQuantity(User user, Integer cartId, Integer quantity) {
        Cart item = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));
        item.setSoLuong(quantity);
        item.capNhatTongTien();
        cartRepo.save(item);
    }

    @Override
    public void removeItem(Integer cartId) {
        cartRepo.deleteById(cartId);
    }

    @Override
    public void clearCart(User user) {
        List<Cart> carts = getCartByUser(user);
        cartRepo.deleteAll(carts);
    }
    
    @Override
    public Double getTotal(User user) {
        return cartRepo.findByNguoiDung(user).stream()
                .map(c -> c.getTongTien().doubleValue())
                .reduce(0.0, Double::sum);
    }
    
    @Override
    public int getCartCount(User user) {
        return cartRepo.countByNguoiDung(user);
    }

}
