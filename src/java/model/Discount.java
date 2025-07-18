package model;

import java.util.Date;

public class Discount {
    private String id;
    private String name;
    private String description;
    private double discountPercent;
    private Date startDate;
    private Date endDate;
    private boolean active;
    
    // Getters & Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getDiscountPercent() {
        return discountPercent;
    }
    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    // New method to check if current date is within the discount period
    public boolean isWithinDateRange() {
        Date now = new Date();
        return (startDate != null && endDate != null) &&
               !now.before(startDate) &&  // now >= startDate
               !now.after(endDate);       // now <= endDate
    }
    
    private String type; // e.g. "PERCENT", "AMOUNT", etc.

public String getType() { return type; }
public void setType(String type) { this.type = type; }

}
