package com.campus.secondhand.repository;

import com.campus.secondhand.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findByStatus(Integer status, Pageable pageable);
    
    Page<Product> findBySellerId(Long sellerId, Pageable pageable);
    
    Page<Product> findByStatusAndCategory(Integer status, String category, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND (p.title LIKE %?1% OR p.description LIKE %?1%)")
    Page<Product> search(String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.createTime DESC")
    Page<Product> findLatest(Pageable pageable);
    
    List<Product> findByStatusIn(List<Integer> statuses);
    
    /**
     * 统计指定状态的商品数量
     */
    long countByStatus(Integer status);
    
    /**
     * 查询未删除的商品（逻辑删除过滤）
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.status = ?1")
    Page<Product> findByStatusAndNotDeleted(Integer status, Pageable pageable);
    
    /**
     * 查询卖家的未删除商品
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.sellerId = ?1")
    Page<Product> findBySellerIdAndNotDeleted(Long sellerId, Pageable pageable);
    
    /**
     * 查询卖家的指定状态且未删除的商品
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.sellerId = ?1 AND p.status = ?2")
    Page<Product> findBySellerIdAndStatusAndNotDeleted(Long sellerId, Integer status, Pageable pageable);
}
