package fourpetals.com.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fourpetals.com.entity.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart , Integer>{
	Optional<Cart> findByNguoiDung_UserIdAndSanPham_MaSP(Integer userId, Integer maSP);

}
