package fourpetals.com.service;

import java.util.List;
import java.util.Optional;

import fourpetals.com.entity.Review;
import fourpetals.com.entity.Product;
import fourpetals.com.entity.User;

public interface ReviewService {
	List<Review> getReviewsByProduct(Product product);

	Double getAverageRating(Integer productId);

	Review addReview(Product product, User user, Integer rating, String comment);
}
