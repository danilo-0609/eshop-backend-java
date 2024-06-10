package com.eshop.backend.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//Clase que representa la petición que hace el cliente al servidor para crear un producto
public class ProductRequest {
    private String sellerName;
    private String name;
    private BigDecimal price;
    private String description;
    private String productType;
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

    
    public String getProductType() {
        return productType;
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
    	this.productType = productType;
    	this.inStock = inStock;
    }
}
