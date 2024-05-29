package com.eshop.backend.modules.catalog.application.products;

//Clase que representa la petición que hace el cliente al servidor para vender un producto
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
