package com.eshop.backend.application;

import java.math.BigDecimal;

public class GetProductByPriceRequest {
	
	private BigDecimal maxPrice;
	private BigDecimal minPrice;
	
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public GetProductByPriceRequest(BigDecimal maxPrice, BigDecimal minPrice) {
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
	}
}
