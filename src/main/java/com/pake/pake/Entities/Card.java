package com.pake.pake.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_number")
    private String cardNumber;

    private String cvv;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "balance")
    private Double balance = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}