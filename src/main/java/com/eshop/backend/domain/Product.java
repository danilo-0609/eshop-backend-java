package com.eshop.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



import com.eshop.backend.buildingBlocks.IBusinessRule;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;
import com.eshop.backend.domain.rules.ProductCannotBePublishedWithNoStockRule;
import com.eshop.backend.domain.rules.ProductCannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStockRule;
import com.eshop.backend.domain.rules.ProductCannotBeSoldWhenProductIsOutOfStockRule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * Entidad del proyecto con lógica de negocio. Se encarga de validar la publicación de productos, evaluar su cantidad en inventario,
 * el estado de su inventario y actualizar sus datos.
 * 
 * Mantiene sus atributos privados para garantizar que solamente esta entidad puede hacer cambio de sus entidades, para garantizar
 * consistencia.
 */
@Entity
@Table(name="products")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "productId", updatable = false, nullable = false)
    private UUID id;
	
	@Column(name = "seller_name")
    private String sellerName;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "price")
	private BigDecimal price;
    
	@Column(name = "description")	
	private String description;
    
	@Column(name = "product_type")
	private String productType;
	
	@Column(name = "in_stock")
	private int inStock;
    
	@Column(name = "stock_status")
	@Enumerated(EnumType.STRING)
	private StockStatus stockStatus;

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

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    
    //Crea productos y valida que estos puedan ser creados y publicados.
    public static Result<Product> publish(String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            String productType, 
            int inStock, 
            LocalDateTime occurredOn) {
        
    		UUID productId = UUID.randomUUID();
    	
        Product product = new Product(productId, 
                sellerName, 
                name, 
                price, 
                description, 
                productType, 
                inStock);
        
        ProductCannotBePublishedWithNoStockRule rule = new ProductCannotBePublishedWithNoStockRule(inStock);
        
        Result<Success> cannotBePublishedWithNoStock = product
                .checkRule(rule);
        
        //Un producto no puede ser publicado si no tiene inventario actualmente
        if (cannotBePublishedWithNoStock.isError()) {
            return Result.error(cannotBePublishedWithNoStock.getFirstError());
        }

        return Result.success(product);
    }
    
    
    public static Product Create(UUID id, 
            String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            String productType, 
            int inStock) {
    	
    	return new Product(id, 
                sellerName, 
                name, 
                price, 
                description, 
                productType, 
                inStock);
    }
    
    //Actualiza el producto
    public static Product update(UUID id, 
            String sellerName, 
            String name, 
            BigDecimal price, 
            String description, 
            String productType, 
            int inStock) {
        
        return new Product(id, 
                sellerName, 
                name, 
                price, 
                description, 
                productType, 
                inStock);
    }

    
    //Se encarga de validar que un producto pueda ser vendido. Toma como argumentos la cantidad de productos a comprar.
    public Result<Void> sell(int amountOfProducts) {
        
    	//Regla de negocio que valida si el producto tiene inventario suficiente para vender. Si esto es falso, se retorna un error.
    	Result<Success> isOutOfStockRule = checkRule(new ProductCannotBeSoldWhenProductIsOutOfStockRule(stockStatus));

        if (isOutOfStockRule.isError()) {
            return Result.error(isOutOfStockRule.getFirstError());
        }
        
        //Regla de negocio que valida si la cantidad de productos solicitada para la compra es mayor a la cantidad de productos en el inventario actualmente.
        //Si esto es cierto, se retorna un error.
        Result<Success> isAmountOfProductsRequestedGreaterThanActualInStock = checkRule(new ProductCannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStockRule(amountOfProducts, inStock));

        if (isAmountOfProductsRequestedGreaterThanActualInStock.isError()) {
            return Result.error(isAmountOfProductsRequestedGreaterThanActualInStock.getFirstError());
        }
        
        
        //Se ajusta la cantidad de productos en el inventario.
        inStock -= amountOfProducts;
        
        //Se cambia el estado del stock de ser necesario
        this.setStatus();
        return Result.success();
    }

    private StockStatus checkStatus() {
        return this.inStock == 0 ? StockStatus.OUT_OF_STOCK : StockStatus.WITH_STOCK;
    }
    
    //Si la cantidad de productos en el inventario es igual a 0, el estado del inventario es OUT_OF_STOCK (sin inventario)
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
            String productType, 
            int inStock) {
        this.id = id;
        this.sellerName = sellerName;
        this.name = name;
        this.price = price;
        this.description = description;
        this.productType = productType;
        this.inStock = inStock;
        this.stockStatus = checkStatus();
    }

    public Product() {}

    
    //Método que ayuda a validar reglas de negocio al determinar si estas han sido rotas o no
    private Result<Success> checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            return Result.error(rule.getError());
        }
        return Result.success();
    }
}

