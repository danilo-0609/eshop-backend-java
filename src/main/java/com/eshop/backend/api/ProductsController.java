package com.eshop.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;
import com.eshop.backend.modules.catalog.application.products.ProductDto;
import com.eshop.backend.modules.catalog.application.products.ProductRequest;
import com.eshop.backend.modules.catalog.application.products.ProductService;
import com.eshop.backend.modules.catalog.application.products.SellProductRequest;

@RestController
public class ProductsController {
	
	private final ProductService productService;
	
	@Autowired
	public ProductsController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products/get/{id}")
	public ProductDto getProduct(@PathVariable UUID id) {
		ProductDto result = productService.getProduct(id);
	
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product was not found");
		}
		
		return result;
	}
	
	@GetMapping
	("/products/all")
	public List<ProductDto> getAllProducts(){
		List<ProductDto> results = productService.getAllProducts();
		
		return results;
	}
	
	@PostMapping("/products/publish")
	public UUID publishProduct(@RequestBody ProductRequest request) {
		
		Result<UUID> result = productService.publishProduct(request);
		
		if (result.isError()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getFirstError().getDescription());
		}
		
		return result.getValue().get();
	}
	
	@PostMapping("/products/sell/{id}")
	public ResponseEntity<Void> sellProduct(@PathVariable UUID id, @RequestBody SellProductRequest request){
		
		Result<Success> result = productService.sellProduct(id, request.getAmountOfProducts());
		
		if (result.isError()) {
			
			if (result.getFirstError() == Error.NotFound("Product.NotFound", "Product was not found")) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, result.getFirstError().getDescription());
			}
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getFirstError().getDescription());
		}
		
		return ResponseEntity.noContent().build();
	}
}
