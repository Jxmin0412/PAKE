package com.pake.pake.DTO;

import lombok.Data;

@Data
public class CardRequest {
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
}