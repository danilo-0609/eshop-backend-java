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

import com.eshop.backend.application.GetProductByPriceRequest;
import com.eshop.backend.application.ProductDto;
import com.eshop.backend.application.ProductRequest;
import com.eshop.backend.application.ProductService;
import com.eshop.backend.application.SellProductRequest;
import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;

@RestController
public class ProductsController {
	
	//Inyecta el servicio "ProductService" al controlador. Este servicio comunica con la capa de dominio o lógica de negocio.
	@Autowired
	private ProductService productService;
	
	
	@GetMapping("/products/get/{id}")
	public ProductDto getProduct(@PathVariable UUID id) {
		
		Result<ProductDto> result = productService.getProduct(id);
	
		//Si el servicio retorna un error, se retorna un código de error 404 NOT FOUND
		if (result.isError()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, result.getFirstError().getCode() + "" + result.getFirstError().getDescription());
		}
		
		
		//Si la operación es exitosa, se retorna 200 OK
		return result.getValue().get();
	}
	
	@GetMapping("/products/get/productType/{productType}")
	public List<ProductDto> getProductByProductType(@PathVariable String productType) {
		
		Result<List<ProductDto>> result = productService.getProductByProductType(productType);
	
		//Si el servicio retorna un error, se retorna un código de error 404 NOT FOUND
		if (result.isError()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, result.getFirstError().getCode() + "" + result.getFirstError().getDescription());
		}
		
		
		//Si la operación es exitosa, se retorna 200 OK
		return result.getValue().get();
	}
	
	@PostMapping("/products/get/price")
	public List<ProductDto> getProductByPrice(@RequestBody GetProductByPriceRequest request){
		
		Result<List<ProductDto>> result = productService.getProductByPrice(request.getMinPrice(), request.getMaxPrice());
		
		//Si el servicio retorna un error, se retorna un código de error 404 NOT FOUND
		if (result.isError()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, result.getFirstError().getCode() + "" + result.getFirstError().getDescription());
		}
		
		
		//Si la operación es exitosa, se retorna 200 OK
		return result.getValue().get();
	}
	
	@PostMapping("/products/publish")
	public UUID publishProduct(@RequestBody ProductRequest request) {
		
		Result<UUID> result = productService.publishProduct(request);
		
		//Si el servicio retorna un error, se devuelve un código de error 400 BAD REQUEST
		if (result.isError()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getFirstError().getCode() + "" + result.getFirstError().getDescription());
		}
		
		//Si la operación es exitosa, se retorna 200 OK
		return result.getValue().get();
	}
	
	@PostMapping("/products/sell/{id}")
	public ResponseEntity<Void> sellProduct(@PathVariable UUID id, @RequestBody SellProductRequest request){
		
		Result<Success> result = productService.sellProduct(id, request.getAmountOfProducts());
		
		
		if (result.isError()) {
			
			//Si el error es igual a "NotFound", se retorna un código de error 404 NOT FOUND
			if (result.getFirstError() == Error.NotFound("Product.NotFound", "Product was not found")) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, result.getFirstError().getDescription());
			}
			
			//Si el error no es NotFound, se retorna 400 BAD REQUEST
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getFirstError().getDescription());
		}
		
		//Si la operación es exitosa, se retorna un 204 No Content 
		return ResponseEntity.noContent().build();
	}
}
