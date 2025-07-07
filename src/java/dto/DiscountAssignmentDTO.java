package dto;

import java.util.Date;

public class DiscountAssignmentDTO {
    private String id;
    private String discountId;
    private String itemId;
    private String categoryId;
    private String type; // "ITEM", "CATEGORY", or "ALL"
    private Date createdAt;

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDiscountId() {
        return discountId;
    }
    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    private String displayName;

public String getDisplayName() {
    return displayName;
}

public void setDisplayName(String displayName) {
    this.displayName = displayName;
}

    
    
}
