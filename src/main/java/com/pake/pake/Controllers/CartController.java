package com.pake.pake.Controllers;

import com.pake.pake.DTO.CartDTO;
import com.pake.pake.Entities.Cart;
import com.pake.pake.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/items")
    public ResponseEntity<List<CartDTO>> getCartItems() {
        return ResponseEntity.ok(cartService.getAllCartItems());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDTO dto) {
        try {
            cartService.addToCart(dto.getProductId(), dto.getQuantity());
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}