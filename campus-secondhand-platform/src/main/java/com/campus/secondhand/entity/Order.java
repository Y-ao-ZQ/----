package com.campus.secondhand.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String orderNo;
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false, length = 200)
    private String productTitle;
    
    @Column(nullable = false)
    private Long buyerId;
    
    @Column(length = 50)
    private String buyerName;
    
    @Column(nullable = false)
    private Long sellerId;
    
    @Column(length = 50)
    private String sellerName;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal price;
    
    @Column(nullable = false)
    private Integer status = 0;
    
    @Column(length = 255)
    private String remark;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime payTime;
    private LocalDateTime finishTime;
}
