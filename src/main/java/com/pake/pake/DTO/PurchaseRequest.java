package com.pake.pake.DTO;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long productId;
    private int quantity;
    private double totalCost;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}