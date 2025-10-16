package fourpetals.com.service;

import java.util.List;

import fourpetals.com.entity.Cart;
import fourpetals.com.entity.User;

public interface CartService {
    void addToCart(User user, Integer productId, Integer quantity);
    List<Cart> getCartByUser(User user);
    void removeItem(Integer cartId);
    void clearCart(User user);
}
