package com.eshop.backend.domain.rules;

import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.IBusinessRule;


//Regla de negocio que valida que un producto no se puede publicar si este no tiene ni una unidad
public class ProductCannotBePublishedWithNoStockRule implements IBusinessRule {
	
	private final int amountOfProducts;
	
	public ProductCannotBePublishedWithNoStockRule(int amountOfProducts) {
		this.amountOfProducts = amountOfProducts;
	}

	@Override
	public String getMessage() {
		
		return "Product cannot be published with no stock";
	}

	@Override
	public Error getError() {
		
		return Error.Validation("Product.CannotBePublishedWithNoStock", this.getMessage());
	}

	@Override
	public boolean isBroken() {
		
		if (amountOfProducts == 0) {
			return true;
		}
		
		return false;
	}
	
	
}
