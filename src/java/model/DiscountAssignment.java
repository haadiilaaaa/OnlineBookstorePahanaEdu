
package model;
import java.util.Date;





public class DiscountAssignment {
    private String id;
    private String discountId;
    private String itemId; // nullable
    private String categoryId; // nullable
    private String type; // 'ITEM', 'CATEGORY', 'ALL'
    private Date createdAt;

    // Getters
    public String getId() {
        return id;
    }

    public String getDiscountId() {
        return discountId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

