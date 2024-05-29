package com.eshop.backend.modules.catalog.domain.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.eshop.backend.modules.catalog.domain.products.rules.*;
import com.eshop.backend.buildingBlocks.IBusinessRule;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;

public final class Product {
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
    private StockStatus stockStatus;
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

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public static Result<Product> publish(String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            List<String> sizes, 
            String productType, 
            List<String> tags, 
            int inStock, 
            LocalDateTime occurredOn, 
            List<String> colors) {
        
        Product product = new Product(UUID.randomUUID(), 
                sellerName, 
                name, 
                price, 
                description, 
                sizes, 
                colors, 
                productType, 
                tags, 
                inStock, 
                occurredOn, 
                null);
        
        ProductCannotBePublishedWithNoStockRule rule = new ProductCannotBePublishedWithNoStockRule(inStock);
        
        Result<Success> cannotBePublishedWithNoStock = product
                .checkRule(rule);

        if (cannotBePublishedWithNoStock.isError()) {
            return Result.error(cannotBePublishedWithNoStock.getFirstError());
        }

        return Result.success(product);
    }

    public static Product update(UUID id, 
            String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            List<String> sizes, 
            String productType, 
            List<String> tags, 
            int inStock, 
            LocalDateTime createdOn, 
            LocalDateTime updatedOn, 
            List<String> colors) {
        
        return new Product(id, 
                sellerName, 
                name, 
                price, 
                description, 
                sizes, 
                colors, 
                productType, 
                tags, 
                inStock, 
                createdOn, 
                updatedOn);
    }

    public Result<Void> sell(int amountOfProducts) {
        Result<Success> isOutOfStockRule = checkRule(new ProductCannotBeSoldWhenProductIsOutOfStockRule(stockStatus));

        if (isOutOfStockRule.isError()) {
            return Result.error(isOutOfStockRule.getFirstError());
        }

        Result<Success> isAmountOfProductsRequestedGreaterThanActualInStock = checkRule(new ProductCannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStockRule(amountOfProducts, inStock));

        if (isAmountOfProductsRequestedGreaterThanActualInStock.isError()) {
            return Result.error(isAmountOfProductsRequestedGreaterThanActualInStock.getFirstError());
        }

        inStock -= amountOfProducts;
        
        this.setStatus();
        return Result.success();
    }

    private StockStatus checkStatus() {
        return this.inStock == 0 ? StockStatus.OUT_OF_STOCK : StockStatus.WITH_STOCK;
    }
    
    private void setStatus() {
    	
    	if (this.inStock == 0) {
    		this.stockStatus = StockStatus.OUT_OF_STOCK;
    	}
    }

    private Product(UUID id, 
            String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            List<String> sizes, 
            List<String> colors, 
            String productType, 
            List<String> tags, 
            int inStock, 
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
        this.stockStatus = checkStatus();
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    private Product() {}

    private Result<Success> checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            return Result.error(rule.getError());
        }
        return Result.success();
    }
}

