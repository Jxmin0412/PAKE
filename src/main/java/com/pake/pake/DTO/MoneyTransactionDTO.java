package com.pake.pake.DTO;

import lombok.Data;

@Data
public class MoneyTransactionDTO {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;
    private Double amount;
}