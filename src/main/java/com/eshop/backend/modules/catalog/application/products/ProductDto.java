package com.eshop.backend.modules.catalog.application.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class ProductDto {
	private UUID id;
    private String sellerName;
    private String name;
    private BigDecimal price;
    private String description;
    private List<String> sizes;
    private List<String> colors;
    private String productType;
    private List<String> tags;
    private int inStock;
    private String stockStatus;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    
    public UUID getId() {
        return id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<String> getColors() {
        return colors;
    }

    public String getProductType() {
        return productType;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getInStock() {
        return inStock;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }
    
    public ProductDto(UUID id,
    		String sellerName,
    		String name,
    		BigDecimal price,
    		String description,
    		List<String> sizes,
    		List<String> colors,
    		String productType,
    		List<String> tags,
    		int inStock,
    		String stockStatus,
    		LocalDateTime createdDateTime,
    		LocalDateTime updatedDateTime) {
    	
    	this.id = id;
    	this.sellerName = sellerName;
    	this.name = name;
    	this.price = price;
    	this.description = description;
    	this.sizes = sizes;
    	this.colors = colors;
    	this.productType = productType;
    	this.tags = tags;
    	this.inStock = inStock;
    	this.stockStatus = stockStatus;
    	this.createdDateTime = createdDateTime;
    	this.updatedDateTime = updatedDateTime;
    }
}
