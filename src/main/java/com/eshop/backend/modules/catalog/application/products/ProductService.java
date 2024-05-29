package com.eshop.backend.modules.catalog.application.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;
import com.eshop.backend.modules.catalog.domain.products.*;

/*
 * Capa intermedia entre la API y las entidades de dominio del proyecto.
 * Se encarga de separar responsabilidades, manteniendo a la capa de entidades de dominio del proyecto alejadas de la API
 */

@Service
public class ProductService {
	
	
	//Persistencia local en memoria de productos que permiten a los usuarios mirar productos en el sistema, 
	//comprarlos o crear nuevos
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
	
	
	//Obtiene un producto de acuerdo a su id.
	//Si no existe un producto con el id pasado, se retorna Not Found "No encontrado"
	public Result<ProductDto> getProduct(UUID id) {
		
		for (Product product: products) {
			if (product.getId().equals(id)) {
				
				return Result.success(new ProductDto(
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
						product.getUpdatedDateTime()));
			}
		}
		
		return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
	}
	
	//Publica productos en el sistema que luego se guardan en la lista en memoria de productos.
	//Si el metodo publish() de la entidad de dominio retorna un error, luego este se devuelve
	public Result<UUID> publishProduct(ProductRequest request) {
		
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
			return Result.error(product.getFirstError());
		}
		
		products.add(product.getValue().get());
	
		return Result.success(product.getValue().get().getId());
	}
	
	//Obtiene todos los productos al momento disponibles en memoria 
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
	
	//Vende un producto al usuario. Si no existe un producto que tenga un id igual al id dado en la petición, se retorna un error 
	//"no encontrado". Se llama al método sell() en la entidad de dominio. Si este retorna un error, se devuelve
	public Result<Success> sellProduct(UUID id, int amountOfProducts){
		
		Product product = null;
		
		for (Product p: products) {
			if (p.getId().equals((id))){
				product = p;
			}
		}
		
		if (product == null) {
			return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
		}
		
		Result<Void> sellOperation = product.sell(amountOfProducts);
	
		if (sellOperation.isError()) {
			return Result.error(sellOperation.getFirstError());
		}
		
		return Result.success();
	}
}
