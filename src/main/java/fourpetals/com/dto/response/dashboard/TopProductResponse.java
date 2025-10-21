package fourpetals.com.dto.response.dashboard;

import java.math.BigDecimal;

public class TopProductResponse {
    private Integer productId;
    private String productName;
    private Long totalQuantity;
    private BigDecimal totalRevenue;
    private String imageUrl; // nếu có
    // getters/setters/ctor
    public TopProductResponse() {}
    public TopProductResponse(Integer id, String name, Long qty, BigDecimal rev, String img){
        this.productId=id; this.productName=name; this.totalQuantity=qty; this.totalRevenue=rev; this.imageUrl=img;
    }
    public Integer getProductId(){ return productId; }
    public void setProductId(Integer v){ this.productId=v; }
    public String getProductName(){ return productName; }
    public void setProductName(String v){ this.productName=v; }
    public Long getTotalQuantity(){ return totalQuantity; }
    public void setTotalQuantity(Long v){ this.totalQuantity=v; }
    public BigDecimal getTotalRevenue(){ return totalRevenue; }
    public void setTotalRevenue(BigDecimal v){ this.totalRevenue=v; }
    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String v){ this.imageUrl=v; }
}
