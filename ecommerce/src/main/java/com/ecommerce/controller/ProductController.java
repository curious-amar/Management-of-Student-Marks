package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import java.io.File;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {	
    	
    	System.out.println(name);
    	System.out.println(category);
    	System.out.println(price);

        try {
            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setDescription(description);

            if (!image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                String uploadDirPath = new File("src/main/resources/static/uploads").getAbsolutePath();
            	File uploadDir = new File(uploadDirPath);
            	if (!uploadDir.exists()) uploadDir.mkdirs();

            	String filePath = uploadDirPath + File.separator + fileName;
            	image.transferTo(new File(filePath));

            	product.setImageUrl("/uploads/" + fileName); // for frontend access
            }
            
            productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)	
                    .body("Error while creating product: " + e.getMessage());
        }
    }
    
    @GetMapping("/all-products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.ok(products); // 200 OK
        }
    }


}
