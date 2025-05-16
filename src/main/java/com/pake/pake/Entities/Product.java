package com.pake.pake.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String category;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;


}