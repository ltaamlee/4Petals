package fourpetals.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import fourpetals.com.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT AVG(r.danhGia) FROM Review r WHERE r.sanPham.maSP = :productId")
    Double findAverageRating(Integer productId);
}
