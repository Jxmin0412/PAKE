package com.pake.pake.Controllers;

import com.pake.pake.Entities.Card;
import com.pake.pake.Entities.User;
import com.pake.pake.Services.CardService;
import com.pake.pake.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addCard(@RequestBody Card card, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userService.findById(userId);
            Card savedCard = cardService.addCard(card, user);

            response.put("success", true);
            response.put("message", "Card added successfully");
            response.put("redirectUrl", "./user_card.html");
            response.put("card", savedCard);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add card: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserCards(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.badRequest().body(response);
            }

            List<Card> cards = cardService.getUserCards(userId);
            if (cards.isEmpty()) {
                response.put("success", true);
                response.put("message", "No cards found");
                response.put("cards", cards);
            } else {
                response.put("success", true);
                response.put("message", "Cards retrieved successfully");
                response.put("cards", cards);
            }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch cards: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}