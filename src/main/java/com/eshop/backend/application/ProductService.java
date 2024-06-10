package com.eshop.backend.application;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.backend.buildingBlocks.Error;
import com.eshop.backend.buildingBlocks.Result;
import com.eshop.backend.buildingBlocks.Success;
import com.eshop.backend.domain.Product;
import com.eshop.backend.domain.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import com.eshop.backend.domain.*;

/*
 * Capa intermedia entre la API y las entidades de dominio del proyecto.
 * Se encarga de separar responsabilidades, manteniendo a la capa de entidades de dominio del proyecto alejadas de la API
 */

@Service
public class ProductService {
	
	@PersistenceContext 
	private EntityManager entityManager;

	
	//Obtiene un producto de acuerdo a su id.
	//Si no existe un producto con el id pasado, se retorna Not Found "No encontrado"
	@Transactional
	public Result<ProductDto> getProduct(UUID id) {
		
		String sql = "SELECT * FROM Products"
				+ " WHERE productId = " + "'" + id.toString() + "'" + ";";
        Query query = entityManager.createNativeQuery(sql);
                
        try {
        	Object[] result = (Object[]) query.getSingleResult();
        	
        	UUID productId = UUID.fromString( (String) result[0]);
            String sellerName = (String) result[1];
            String name = (String) result[2];
            BigDecimal price = (BigDecimal) result[3];
            String description = (String) result[4];
            String productType = (String) result[5];
            int inStock = (int) result[6];
            String stockStatus = (String) result[7];
            
            		
    		ProductDto dto = new ProductDto(
    				productId,
    				sellerName,
    				name,
    				price,
    				description,
    				productType,
    				inStock,
    				stockStatus);
    		
    		return Result.success(dto);
        	
        } catch(NoResultException ex) {
			return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
        }
		
	}
	
	//Publica productos en el sistema que luego se guardan en la lista en memoria de productos.
	//Si el metodo publish() de la entidad de dominio retorna un error, luego este se devuelve
	@Transactional
	public Result<UUID> publishProduct(ProductRequest request) {
		
		Result<Product> product = Product.publish(request.getSellerName(), 
				request.getName(), 
				request.getPrice(), 
				request.getDescription(), 
				request.getProductType(), 
				request.getInStock(), 
				LocalDateTime.now());
		
		if (product.isError()) {
			return Result.error(product.getFirstError());
		}

        Product result = product.getValue().get();
        
        String sql = "INSERT INTO products (productId, seller_name, name, price, description, product_type, in_stock, stock_status) "
                + "VALUES" + "(" + "'" + result.getId().toString() + "'" + ", " 
        		+ "'" + result.getSellerName() + "'"  + ", " + "'" + result.getName() + "'" 
        		+ ", " +  result.getPrice() + ", " 
        		+ "'" + result.getDescription() + "'" + ", " 
        		+ "'" + result.getProductType() + "'"  + ", "
        		+ result.getInStock() + ", "
        		+ "'" + result.getStockStatus().name() + "'" + ")";
        
        entityManager.createNativeQuery(sql)
     		.executeUpdate();
     	
		return Result.success(result.getId());
	}
	
	//Vende un producto al usuario. Si no existe un producto que tenga un id igual al id dado en la petición, se retorna un error 
	//"no encontrado". Se llama al método sell() en la entidad de dominio. Si este retorna un error, se devuelve
	@Transactional
	public Result<Success> sellProduct(UUID id, int amountOfProducts){
		
		Result<ProductDto> result = this.getProduct(id);
		
		if (result == null) {
			return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
		}
		
		Product product = Product.Create(id, 
				result.getValue().get().getSellerName(), 
				result.getValue().get().getName(), 
				result.getValue().get().getPrice(), 
				result.getValue().get().getDescription(), 
				result.getValue().get().getProductType(), 
				result.getValue().get().getInStock());
		
		Result<Void> sellOperation = product.sell(amountOfProducts);
	
		if (sellOperation.isError()) {
			return Result.error(sellOperation.getFirstError());
		}
		
		String sql = "UPDATE products "
				+ "SET in_stock = " + product.getInStock() + 
				" WHERE productId = " + " '" + product.getId() + "'" + ";";
		
		entityManager.createNativeQuery(sql)
 		.executeUpdate();
		
		return Result.success();
	}
	
	@Transactional
	public Result<List<ProductDto>> getProductByProductType(String productType){
		
		String sql = "SELECT * FROM Products"
				+ " WHERE product_type = " + "'" + productType + "'" + ";";
        Query query = entityManager.createNativeQuery(sql);
                
        try {
        	
        	List<ProductDto> productsDto = new ArrayList<ProductDto>();
        	
        	List<Object[]> results = query.getResultList();
        	
        	if (results.isEmpty()) {
        		return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
        	}
        	
        	for (Object[] row : results) {
        		
        		UUID productId = UUID.fromString( (String) row[0]);
                String sellerName = (String) row[1];
                String name = (String) row[2];
                BigDecimal price = (BigDecimal) row[3];
                String description = (String) row[4];
                int inStock = (int) row[6];
                String stockStatus = (String) row[7];
                
                		
        		ProductDto dto = new ProductDto(
        				productId,
        				sellerName,
        				name,
        				price,
        				description,
        				productType,
        				inStock,
        				stockStatus);
        		
        		productsDto.add(dto);
        	}
        	
        	
    		return Result.success(productsDto);
        	
        } catch(NoResultException ex) {
			return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
        }
	}
	
	@Transactional
	public Result<List<ProductDto>> getProductByPrice(BigDecimal minPrice, BigDecimal maxPrice){
		String sql = "SELECT * FROM Products"
				+ " WHERE price > " + minPrice + " and price < " + maxPrice;
        Query query = entityManager.createNativeQuery(sql);
                
        try {
        	
        	List<ProductDto> productsDto = new ArrayList<ProductDto>();
        	
        	List<Object[]> results = query.getResultList();
        	
        	if (results.isEmpty()) {
        		return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
        	}
        	
        	for (Object[] row : results) {
        		
        		UUID productId = UUID.fromString( (String) row[0]);
                String sellerName = (String) row[1];
                String name = (String) row[2];
                BigDecimal price = (BigDecimal) row[3];
                String description = (String) row[4];
                String productType = (String) row[5];
                int inStock = (int) row[6];
                String stockStatus = (String) row[7];
                
                		
        		ProductDto dto = new ProductDto(
        				productId,
        				sellerName,
        				name,
        				price,
        				description,
        				productType,
        				inStock,
        				stockStatus);
        		
        		productsDto.add(dto);
        	}
        	
        	
    		return Result.success(productsDto);
        	
        } catch(NoResultException ex) {
			return Result.error(Error.NotFound("Product.NotFound", "Product was not found"));
        }
	}
}
