package fourpetals.com.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Cart;
import fourpetals.com.entity.User;

@Repository
	
public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	List<Cart> findByNguoiDung(User user);

	Optional<Cart> findByNguoiDung_UserIdAndSanPham_MaSP(Integer userId, Integer maSP);

	int countByNguoiDung(User user);
	
	Optional<Cart> findByNguoiDungAndSanPham_MaSP(User nguoiDung, Integer maSP);


}
