package com.campus.secondhand.dto;

import lombok.Data;

@Data
public class LoginDTO {
    
    private String username;
    
    private String password;
    
    private String phone;
    
    private String email;
    
    private String code;
}
