package com.campus.secondhand.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long senderId;
    
    @Column(length = 50)
    private String senderName;
    
    @Column(nullable = false)
    private Long receiverId;
    
    @Column(length = 50)
    private String receiverName;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(nullable = false)
    private Integer status = 0;
    
    @Column
    private Long productId;
    
    @Column(length = 200)
    private String productTitle;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
}
