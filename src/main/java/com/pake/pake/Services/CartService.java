package com.pake.pake.Services;

import com.pake.pake.DTO.CartDTO;
import com.pake.pake.Entities.Cart;
import com.pake.pake.Entities.Product;
import com.pake.pake.Repository.CartRepository;
import com.pake.pake.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartDTO> getAllCartItems() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(this::convertToCartDTO)
                .collect(Collectors.toList());
    }

    private CartDTO convertToCartDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setProductId(cart.getProduct().getId());
        dto.setName(cart.getProduct().getName());
        dto.setQuantity(cart.getQuantity());
        dto.setTotalCost(cart.getTotalCost());

        if (cart.getImage() != null) {
            dto.setImageBase64(Base64.getEncoder().encodeToString(cart.getImage()));
        }

        return dto;
    }

    @Transactional
    public void addToCart(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setTotalCost(product.getPrice() * quantity);
        cart.setImage(product.getImage()); // Copy image from product

        cartRepository.save(cart);
    }
}