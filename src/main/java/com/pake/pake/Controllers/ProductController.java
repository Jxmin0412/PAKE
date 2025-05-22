package com.pake.pake.Controllers;

import com.pake.pake.DTO.ProductDTO;
import com.pake.pake.Entities.Product;
import com.pake.pake.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("image") MultipartFile image) {

        Map<String, Object> response = new HashMap<>();

        try {
            Product product = productService.addProduct(name, description, price, quantity, "general", image);
            response.put("success", true);
            response.put("message", "Product added successfully");
            response.put("product", product);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        List<ProductDTO> productDTOs = products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setQuantity(product.getQuantity());
            dto.setCategory(product.getCategory());


            // Convert byte[] to Base64 string
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                dto.setImageBase64(base64Image);
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }
    @PostMapping("/purchase/{productId}")
    public ResponseEntity<Map<String, Object>> purchaseProduct(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();


        try {
            Product product = productService.purchaseProduct(productId);
            response.put("success", true);
            response.put("message", "Product purchased successfully");
            response.put("redirectUrl", "./user_products1.html");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error purchasing product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    // Add this new endpoint
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductDTO productDTO = convertToDTO(product);
            return ResponseEntity.ok(productDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        if (product.getImage() != null) {
            dto.setImageBase64(Base64.getEncoder().encodeToString(product.getImage()));
        }
        return dto;
    }


}
