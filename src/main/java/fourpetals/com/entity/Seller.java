package fourpetals.com.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Seller")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SellerID")
    private Integer sellerId;
    
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
    
    @Column(name = "ShopName", nullable = false, length = 200)
    private String shopName;
    
    @Column(name = "ShopDescription", length = 500)
    private String shopDescription;
    
    @Column(name = "ShopLogo", length = 500)
    private String shopLogo;
    
    @Column(name = "Status")
    private Integer status = 1;
    
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
