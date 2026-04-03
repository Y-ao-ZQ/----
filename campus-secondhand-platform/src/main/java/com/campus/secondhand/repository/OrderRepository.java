package com.campus.secondhand.repository;

import com.campus.secondhand.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNo(String orderNo);
    
    Page<Order> findByBuyerId(Long buyerId, Pageable pageable);
    
    Page<Order> findBySellerId(Long sellerId, Pageable pageable);
    
    List<Order> findByProductId(Long productId);
    
    Page<Order> findByBuyerIdAndStatus(Long buyerId, Integer status, Pageable pageable);
    
    Page<Order> findBySellerIdAndStatus(Long sellerId, Integer status, Pageable pageable);
    
    /**
     * 统计指定时间之后创建的订单数量
     */
    long countByCreateTimeAfter(LocalDateTime startTime);
    
    /**
     * 统计指定时间之后创建且状态在指定范围内的订单总金额
     */
    @Query("SELECT SUM(o.price) FROM Order o WHERE o.createTime >= ?1 AND o.status IN ?2")
    BigDecimal sumByCreateTimeAfterAndStatusIn(LocalDateTime startTime, List<Integer> statuses);
    
    /**
     * 统计状态在指定范围内的订单总金额
     */
    @Query("SELECT SUM(o.price) FROM Order o WHERE o.status IN ?1")
    BigDecimal sumByStatusIn(List<Integer> statuses);
}
