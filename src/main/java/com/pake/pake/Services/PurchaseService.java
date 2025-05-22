package com.pake.pake.Services;

import com.pake.pake.DTO.PurchaseRequest;
import com.pake.pake.Entities.Card;
import com.pake.pake.Entities.Product;
import com.pake.pake.Repository.CardRepository;
import com.pake.pake.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PurchaseService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ProductRepository productRepository;

    public void processPurchase(PurchaseRequest request) throws Exception {
        // 1. Validate card details
        Card card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new Exception("Invalid card details"));

        if (!isValidCard(card, request.getExpiryDate(), request.getCvv())) {
            throw new Exception("Invalid card details");
        }

        // 2. Check sufficient balance
        if (card.getBalance() < request.getTotalCost()) {
            throw new Exception("Insufficient balance");
        }

        // 3. Check product availability
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new Exception("Product not found"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new Exception("Insufficient product quantity");
        }

        // 4. Update balance
        card.setBalance(card.getBalance() - request.getTotalCost());
        cardRepository.save(card);

        // 5. Update product quantity
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);
    }

    private boolean isValidCard(Card card, String expiryDate, String cvv) {
        return card.getExpiryDate().equals(expiryDate) &&
                card.getCvv().equals(cvv);
    }
}