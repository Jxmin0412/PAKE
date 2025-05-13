package com.pake.pake.Controllers;

import com.pake.pake.DTO.MoneyTransactionDTO;
import com.pake.pake.Entities.Card;
import com.pake.pake.Services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/money")
public class MoneyController {

    @Autowired
    private CardService cardService;

    @PostMapping("/add")
    public ResponseEntity<?> addMoney(@RequestBody MoneyTransactionDTO transaction, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (session.getAttribute("userId") == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.badRequest().body(response);
            }

            Card updatedCard = cardService.addMoney(
                    transaction.getCardNumber(),
                    transaction.getCardHolderName(),
                    transaction.getCvv(),
                    transaction.getExpiryDate(),
                    transaction.getAmount()
            );

            response.put("success", true);
            response.put("message", "Money added successfully");
            response.put("card", updatedCard);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add money: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}