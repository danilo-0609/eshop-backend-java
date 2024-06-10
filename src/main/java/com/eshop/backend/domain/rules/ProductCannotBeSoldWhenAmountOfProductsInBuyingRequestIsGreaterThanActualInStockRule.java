package com.eshop.backend.domain.rules;

import com.eshop.backend.buildingBlocks.IBusinessRule;
import com.eshop.backend.domain.StockStatus;
import com.eshop.backend.buildingBlocks.Error;

//Regla de negocio que valida que no se puede vender una cantidad de productos x si esta cantidad es mayor a la cantidad de productos en el inventario
public class ProductCannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStockRule implements IBusinessRule {
    private final int inStock;
    private final int amountOfProductsInBuyingRequest;

    public ProductCannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStockRule(int amountOfProductsInBuyingRequest, int inStock) {
        this.amountOfProductsInBuyingRequest = amountOfProductsInBuyingRequest;
        this.inStock = inStock;
    }

	@Override
    public String getMessage() {
        return "Product cannot be sold when amount of products in buying request is greater than actual in stock amount";
    }

    @Override
    public Error getError() {
        return Error.Validation("Product.CannotBeSoldWhenAmountOfProductsInBuyingRequestIsGreaterThanActualInStock", this.getMessage());
    }

    @Override
    public boolean isBroken() {
        return amountOfProductsInBuyingRequest > inStock;
    }
}
