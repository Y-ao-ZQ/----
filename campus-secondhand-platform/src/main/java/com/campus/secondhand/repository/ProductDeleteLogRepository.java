package com.campus.secondhand.repository;

import com.campus.secondhand.entity.ProductDeleteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品删除日志数据访问接口
 */
@Repository
public interface ProductDeleteLogRepository extends JpaRepository<ProductDeleteLog, Long> {
    
    /**
     * 按商品 ID 查询删除日志
     */
    List<ProductDeleteLog> findByProductId(Long productId);
    
    /**
     * 按操作人 ID 查询删除日志
     */
    Page<ProductDeleteLog> findByOperatorId(Long operatorId, Pageable pageable);
    
    /**
     * 按删除类型查询
     */
    Page<ProductDeleteLog> findByDeleteType(String deleteType, Pageable pageable);
    
    /**
     * 按时间范围查询删除日志
     */
    List<ProductDeleteLog> findByDeleteTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询最近 N 条删除记录
     */
    List<ProductDeleteLog> findTop20ByOrderByDeleteTimeDesc();
    
    /**
     * 统计指定时间范围内的删除数量
     */
    @Query("SELECT COUNT(p) FROM ProductDeleteLog p WHERE p.deleteTime BETWEEN :startTime AND :endTime")
    Long countByDeleteTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);
    
    /**
     * 按商品 ID 和操作人 ID 查询
     */
    List<ProductDeleteLog> findByProductIdAndOperatorId(Long productId, Long operatorId);
}
