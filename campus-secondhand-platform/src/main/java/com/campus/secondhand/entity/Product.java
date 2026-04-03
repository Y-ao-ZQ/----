package com.campus.secondhand.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(length = 50)
    private String category;
    
    @Column(name = "`condition`", length = 50)
    private String condition;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(nullable = false)
    private Long sellerId;
    
    @Column(length = 50)
    private String sellerName;
    
    @Column(length = 255)
    private String images;
    
    @Column(length = 50)
    private String contact;
    
    @Column(length = 100)
    private String location;
    
    @Column(nullable = false)
    private Integer viewCount = 0;
    
    @Column(nullable = false)
    private Integer likeCount = 0;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
