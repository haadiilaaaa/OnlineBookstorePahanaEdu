package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDTO implements Serializable {
    private String id;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private String categoryId;
    private String categoryName;

    // Discount fields
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private boolean hasDiscount;
    private String discountLabel;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(BigDecimal discountedPrice) { this.discountedPrice = discountedPrice; }

    public boolean isHasDiscount() { return hasDiscount; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }

    public String getDiscountLabel() { return discountLabel; }
    public void setDiscountLabel(String discountLabel) { this.discountLabel = discountLabel; }
    
    
   

    
}
