package com.campus.secondhand.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 商品删除日志实体
 * 用于记录所有商品删除操作的详细信息
 */
@Data
@Entity
@Table(name = "product_delete_logs")
public class ProductDeleteLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 被删除商品的 ID
     */
    @Column(nullable = false)
    private Long productId;
    
    /**
     * 商品标题（删除时的快照）
     */
    @Column(nullable = false, length = 200)
    private String productTitle;
    
    /**
     * 商品描述（删除时的快照）
     */
    @Column(columnDefinition = "TEXT")
    private String productDescription;
    
    /**
     * 商品价格（删除时的快照）
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private String productPrice;
    
    /**
     * 商品分类（删除时的快照）
     */
    @Column(length = 50)
    private String productCategory;
    
    /**
     * 商品图片（删除时的快照）
     */
    @Column(length = 255)
    private String productImages;
    
    /**
     * 商品状态（删除时的状态）
     */
    @Column(nullable = false)
    private Integer productStatus;
    
    /**
     * 执行删除操作的用户 ID
     */
    @Column(nullable = false)
    private Long operatorId;
    
    /**
     * 执行删除操作的用户名
     */
    @Column(length = 100)
    private String operatorName;
    
    /**
     * 删除原因
     */
    @Column(length = 500)
    private String deleteReason;
    
    /**
     * 删除类型：USER(用户主动删除), ADMIN(管理员删除), SYSTEM(系统删除)
     */
    @Column(nullable = false, length = 20)
    private String deleteType;
    
    /**
     * 操作时的 IP 地址
     */
    @Column(length = 50)
    private String ipAddress;
    
    /**
     * 操作时的 User-Agent
     */
    @Column(length = 500)
    private String userAgent;
    
    /**
     * 删除时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime deleteTime;
    
    /**
     * 备注信息
     */
    @Column(length = 1000)
    private String remarks;
}
