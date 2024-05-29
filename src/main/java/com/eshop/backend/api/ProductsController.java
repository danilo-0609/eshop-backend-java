package com.eshop.backend.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eshop.backend.modules.catalog.application.products.ProductDto;
import com.eshop.backend.modules.catalog.application.products.ProductService;

@RestController
public class ProductsController {
	
	private final ProductService productService;
	
	@Autowired
	public ProductsController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/get/")
	public ProductDto GetProduct(@RequestParam UUID id) {
		ProductDto result = productService.getProduct(id);
	
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product was not found");
		}
		
		return result;
	}
	
	
}
