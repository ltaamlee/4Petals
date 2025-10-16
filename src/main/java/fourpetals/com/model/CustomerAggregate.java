package fourpetals.com.model;

import java.time.LocalDateTime;

public record CustomerAggregate(
    long totalSpent,       
    long orderCount,        
    LocalDateTime lastOrder 
) {}
