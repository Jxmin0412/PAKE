package com.pake.pake.Entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String gender;
    private String dateOfBirth;
    private String mobile;
    private String email;
    private String filename;
    private String secretKey;
    private String cipherText;
    private String userType = "Normal";

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;


}
