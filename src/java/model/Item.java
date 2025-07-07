package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Item implements Serializable {
    private String id;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private String categoryId;
    private Timestamp createdAt;
    private BigDecimal originalPrice;  // original price before discount
    private BigDecimal discountedPrice; // discounted price
    private boolean hasDiscount;       // flag if discount applies
    private String discountLabel;      // e.g. "20% OFF", "Sale" etc.

    

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

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    private Category category;

public Category getCategory() {
    return category;
}

public void setCategory(Category category) {
    this.category = category;
}
    // getters and setters for these new fields
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(BigDecimal discountedPrice) { this.discountedPrice = discountedPrice; }

    public boolean isHasDiscount() { return hasDiscount; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }

    public String getDiscountLabel() { return discountLabel; }
    public void setDiscountLabel(String discountLabel) { this.discountLabel = discountLabel; }
    private String discountType;
private BigDecimal discountAmount;

public String getDiscountType() {
    return discountType;
}

public void setDiscountType(String discountType) {
    this.discountType = discountType;
}

public BigDecimal getDiscountAmount() {
    return discountAmount;
}

public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
}



}
