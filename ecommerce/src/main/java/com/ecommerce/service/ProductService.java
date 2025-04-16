package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Product;

public interface ProductService {
	
	Product createProduct(Product product);
	
	List<Product> getAllProducts();

}
