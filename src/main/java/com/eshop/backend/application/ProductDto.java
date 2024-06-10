package com.eshop.backend.application;

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
    private String productType;
    private int inStock;
    private String stockStatus;
    
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

    public String getProductType() {
        return productType;
    }

    public int getInStock() {
        return inStock;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public ProductDto(UUID id,
    		String sellerName,
    		String name,
    		BigDecimal price,
    		String description,
    		String productType,
    		int inStock,
    		String stockStatus) {
    	
    	this.id = id;
    	this.sellerName = sellerName;
    	this.name = name;
    	this.price = price;
    	this.description = description;
    	this.productType = productType;
    	this.inStock = inStock;
    	this.stockStatus = stockStatus;
    }
}
