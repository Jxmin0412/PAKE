package com.pake.pake.DTO;

import lombok.Data;

@Data
public class CartDTO {
    private Long id;
    private Long productId;
    private String name;
    private Integer quantity;
    private Double totalCost;
    private String imageBase64;
}