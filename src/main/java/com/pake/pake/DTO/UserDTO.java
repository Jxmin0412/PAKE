package com.pake.pake.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String gender;
    private String dateOfBirth;
    private String mobile;
    private String email;
    private String userType;

}
