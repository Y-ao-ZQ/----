package com.campus.secondhand.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 50)
    private String nickname;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 255)
    private String avatar;
    
    @Column(length = 50)
    private String gender;
    
    @Column(length = 100)
    private String school;
    
    @Column(length = 100)
    private String college;
    
    @Column(length = 20)
    private String grade;
    
    @Column(nullable = false)
    private Integer role = 0;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
