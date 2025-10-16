package fourpetals.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fourpetals.com.entity.Product;
import fourpetals.com.entity.Review;
import fourpetals.com.entity.User;
import fourpetals.com.repository.ReviewRepository;
import fourpetals.com.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Override
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepo.findAll().stream()
                .filter(r -> r.getSanPham().getMaSP().equals(product.getMaSP()))
                .toList();
    }

    @Override
    public Double getAverageRating(Integer productId) {
        Double avg = reviewRepo.findAverageRating(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    @Override
    public Review addReview(Product product, User user, Integer rating, String comment) {
        Review r = new Review();
        r.setSanPham(product);
        r.setNguoiDanhGia(user);
        r.setDanhGia(rating);
        r.setBinhLuan(comment);
        return reviewRepo.save(r);
    }
}
