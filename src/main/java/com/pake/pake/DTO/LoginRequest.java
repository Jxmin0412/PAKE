package com.pake.pake.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String secretKey;
    private String fileName;
    private byte[] fileData;
}