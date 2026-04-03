package com.campus.secondhand.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private BigDecimal price;
    
    private String category;
    
    private String condition;
    
    private String images;
    
    private String contact;
    
    private String location;
}
