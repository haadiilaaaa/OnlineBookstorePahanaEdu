package dto;

import java.math.BigDecimal;

public final class ItemSearchCriteria {
    private final String keyword;
    private final String categoryId;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final int page;
    private final int limit;

    private ItemSearchCriteria(Builder builder) {
        this.keyword = builder.keyword;
        this.categoryId = builder.categoryId;
        this.minPrice = builder.minPrice;
        this.maxPrice = builder.maxPrice;
        this.page = builder.page;
        this.limit = builder.limit;
    }

    // Only getters — no setters!
    public String getKeyword() {
        return keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public static class Builder {
        private String keyword;
        private String categoryId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private int page = 1;     // default
        private int limit = 10;   // default

        public Builder keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder minPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder page(int page) {
            if (page > 0) this.page = page;
            return this;
        }

        public Builder limit(int limit) {
            if (limit > 0 && limit <= 100) this.limit = limit;
            return this;
        }

        public ItemSearchCriteria build() {
            return new ItemSearchCriteria(this);
        }
    }
}
