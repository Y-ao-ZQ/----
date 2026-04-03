package com.campus.secondhand.repository;

import com.campus.secondhand.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
