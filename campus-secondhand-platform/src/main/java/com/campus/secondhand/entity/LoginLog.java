package com.campus.secondhand.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "login_logs")
public class LoginLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String username;
    
    @Column(nullable = false)
    private boolean success;
    
    private String ipAddress;
    
    private String userAgent;
    
    private String failureReason;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime loginTime;
}
