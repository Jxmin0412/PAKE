package com.pake.pake.Services;

import com.pake.pake.Entities.Card;
import com.pake.pake.Entities.User;
import com.pake.pake.Repository.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}