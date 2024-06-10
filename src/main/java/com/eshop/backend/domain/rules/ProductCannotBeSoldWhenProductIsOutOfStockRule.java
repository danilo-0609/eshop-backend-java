package com.eshop.backend.domain.rules;

import com.eshop.backend.buildingBlocks.IBusinessRule;
import com.eshop.backend.domain.StockStatus;
import com.eshop.backend.buildingBlocks.Error;

//Regla de negocio que valida que un producto no se puede vender si no hay unidades. El inventario no puede ser un número negativo.
public class ProductCannotBeSoldWhenProductIsOutOfStockRule implements IBusinessRule {

    private final StockStatus stockStatus;

    public ProductCannotBeSoldWhenProductIsOutOfStockRule(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    @Override
    public Error getError() {
        return Error.Validation("Product.OutOfStock", this.getMessage());  
    }

    @Override
    public boolean isBroken() {
        return this.stockStatus == StockStatus.OUT_OF_STOCK;
    }

    @Override
    public String getMessage() {
        return "Product cannot be sold when product is out of stock";
    }
}
