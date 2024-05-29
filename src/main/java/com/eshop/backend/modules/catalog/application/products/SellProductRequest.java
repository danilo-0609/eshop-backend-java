package com.eshop.backend.modules.catalog.application.products;

public class SellProductRequest {
	
	private int amountOfProducts;

	public int getAmountOfProducts() {
		return amountOfProducts;
	}
	
	public SellProductRequest() {}
	
	public SellProductRequest(int amountOfProducts) {
		this.amountOfProducts = amountOfProducts;
	}
}
