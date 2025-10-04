package fourpetals.com.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ProductID")
	private Integer productId;

	@ManyToOne
	@JoinColumn(name = "CategoryID")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "SellerID")
	private Seller seller;

	@Column(name = "ProductName", nullable = false, length = 200)
	private String productName;

	@Column(name = "ProductCode", unique = true, length = 50)
	private String productCode;

	@Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
	private String description;

	@Column(name = "Price", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(name = "Stock")
	private Integer stock = 0;

	@Column(name = "ImageUrl", length = 500)
	private String imageUrl;

	@Column(name = "Views")
	private Integer views = 0;

	@Column(name = "Likes")
	private Integer likes = 0;

	@Column(name = "Status")
	private Integer status = 1; // 1: Available, 0: Out of stock, -1: Discontinued

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;

	@Column(name = "UpdatedAt")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
