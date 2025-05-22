package com.pake.pake.Services;

import com.pake.pake.Entities.Product;
import com.pake.pake.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Product addProduct(String name, String description, Double price,
                              Integer quantity, String category, MultipartFile image) throws IOException {
        // Generate unique filename
        String fileName =  image.getOriginalFilename();

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get("src/main/resources/products/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }



        // Create and save product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setImage(image.getBytes());



        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product purchaseProduct(Long productId) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));

        if (product.getQuantity() <= 0) {
            throw new Exception("Product out of stock");
        }

        product.setQuantity(product.getQuantity());
        return productRepository.save(product);
    }
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found with id: " + id));
    }


}