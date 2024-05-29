package com.eshop.backend.modules.catalog.application.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductRequest {
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

    
    public ProductRequest() { }
    
    public ProductRequest(String sellerName,
    		String name,
    		String price,
    		String description,
    		List<String> sizes,
    		List<String> colors,
    		String productType,
    		List<String> tags,
    		int inStock) {
    	
    	BigDecimal decimalPrice = new BigDecimal(price);
    		
    	this.sellerName = sellerName;
    	this.name = name;
    	this.price = decimalPrice;
    	this.description = description;
    	this.sizes = sizes;
    	this.colors = colors;
    	this.productType = productType;
    	this.tags = tags;
    	this.inStock = inStock;
    }
}
