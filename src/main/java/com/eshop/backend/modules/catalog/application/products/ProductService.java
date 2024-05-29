package com.eshop.backend.modules.catalog.application.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.modules.catalog.domain.products.*;

public class ProductService {
	
	private List<Product> products;
	
	public ProductService() {
		products = new ArrayList<Product>();
		
		List<String> tags = new ArrayList<String>();
		
		tags.add("#Fit" );
		
		List<String> sizes = new ArrayList<String>();
		
		sizes.add("M" );
		
		List<String> colors = new ArrayList<String>();
		
		colors.add("Azul");
		colors.add("Amarillo");
		colors.add("Verde");
		colors.add("Negro");
		
		Result<Product> product = Product.publish("Danilo", 
				"Camiseta deportiva para hombres y mujeres", 
				new BigDecimal("123.0"),
				"Camiseta fit para hombres y mujeres", 
				sizes, 
				"Camiseta", 
				tags, 
				100, 
				LocalDateTime.now(), 
				colors);
		
		products.add(product.getValue().get());
	}
	
	
	public ProductDto getProduct(UUID id) {
		
		for (Product product: products) {
			if (product.getId() == id) {
				return new ProductDto(
						product.getId(),
						product.getSellerName(),
						product.getName(),
						product.getPrice(),
						product.getDescription(),
						product.getSizes(),
						product.getColors(),
						product.getProductType(),
						product.getTags(),
						product.getInStock(),
						product.getStockStatus().name(),
						product.getCreatedDateTime(),
						product.getUpdatedDateTime());
			}
		}
		
		return null;
	}
	
	
	public UUID publishProduct(ProductRequest request) {
		
		Result<Product> product = Product.publish(request.getSellerName(), 
				request.getName(), 
				request.getPrice(), 
				request.getDescription(), 
				request.getSizes(), 
				request.getProductType(), 
				request.getTags(), 
				request.getInStock(), 
				LocalDateTime.now(), 
				request.getColors());
		
		if (product.isError()) {
			return null;
		}
		
		products.add(product.getValue().get());
	
		return product.getValue().get().getId();
	}
	
	public List<ProductDto> getAllProducts(){
		
		List<ProductDto> productsDto = new ArrayList<ProductDto>();
		
		for (Product product: products) {
			
			ProductDto productDto = new ProductDto(
					product.getId(),
					product.getSellerName(),
					product.getName(),
					product.getPrice(),
					product.getDescription(),
					product.getSizes(),
					product.getColors(),
					product.getProductType(),
					product.getTags(),
					product.getInStock(),
					product.getStockStatus().name(),
					product.getCreatedDateTime(),
					product.getUpdatedDateTime());
			
			productsDto.add(productDto);
		}
		
		return productsDto;
	}
}
