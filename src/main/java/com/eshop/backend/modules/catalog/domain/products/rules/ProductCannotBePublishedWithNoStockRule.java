package com.eshop.backend.modules.catalog.domain.products.rules;

import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.IBusinessRule;

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
