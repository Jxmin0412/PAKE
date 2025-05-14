package com.pake.pake.Services;

import com.pake.pake.DTO.MoneyTransactionDTO;
import com.pake.pake.Entities.Card;
import com.pake.pake.Entities.User;
import com.pake.pake.Repository.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card addCard(Card card, User user) {
        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new RuntimeException("Card already exists");
        }
        card.setUser(user);
        return cardRepository.save(card);
    }

    public List<Card> getUserCards(Long userId) {
        return cardRepository.findByUserId(userId);
    }
    @Transactional
    public Card addMoney(String cardNumber, String cardHolderName, String cvv, String expiryDate, Double amount) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Validate card details
        if (!card.getCardHolderName().equals(cardHolderName) ||
                !card.getCvv().equals(cvv) ||
                !card.getExpiryDate().equals(expiryDate)) {
            throw new RuntimeException("Invalid card details");
        }

        // Update balance
        card.setBalance(card.getBalance() + amount);
        return cardRepository.save(card);
    }
    public ResponseEntity<?> addMoney(MoneyTransactionDTO moneyTransactionDTO) {
        try {
            // Get the card by card number
            Card card = cardRepository.findByCardNumber(moneyTransactionDTO.getCardNumber())
                    .orElseThrow(() -> new RuntimeException("Card not found"));

            // Verify card details
            if (!card.getCardHolderName().equals(moneyTransactionDTO.getCardHolderName()) ||
                    !card.getCvv().equals(moneyTransactionDTO.getCvv()) ||
                    !card.getExpiryDate().equals(moneyTransactionDTO.getExpiryDate())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Invalid card details");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Validate amount
            if (moneyTransactionDTO.getAmount() <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Invalid amount");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Update balance
            card.setBalance(card.getBalance() + moneyTransactionDTO.getAmount());
            cardRepository.save(card);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Money added successfully");
            response.put("card", card);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error processing transaction: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}