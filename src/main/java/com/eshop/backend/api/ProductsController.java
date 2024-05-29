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
	
	//Inyecta el servicio "ProductService" al controlador. Este servicio comunica con la capa de dominio o lógica de negocio.
	private final ProductService productService;
	
	@Autowired
	public ProductsController(ProductService productService) {
		this.productService = productService;
	}
	
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
	
	@GetMapping
	("/products/all")
	public List<ProductDto> getAllProducts(){
		
		List<ProductDto> results = productService.getAllProducts();
		
		//Se retorna 200 OK 
		return results;
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
