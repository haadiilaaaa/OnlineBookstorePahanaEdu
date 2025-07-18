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
  private DiscountMetaData discount;

public DiscountMetaData getDiscount() {
    return discount;
}

public void setDiscount(DiscountMetaData discount) {
    this.discount = discount;
}






}
