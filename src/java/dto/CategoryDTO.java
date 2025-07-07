package dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class CategoryDTO implements Serializable {
    private String id;
    private String name;
    private String description;
    private Timestamp createdAt;

    public CategoryDTO() {}

    public CategoryDTO(String id, String name, String description, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
