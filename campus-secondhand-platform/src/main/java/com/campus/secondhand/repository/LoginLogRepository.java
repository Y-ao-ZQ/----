package com.campus.secondhand.repository;

import com.campus.secondhand.entity.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    
    /**
     * 按用户 ID 查询登录日志
     */
    Page<LoginLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 按用户名查询登录日志
     */
    Page<LoginLog> findByUsername(String username, Pageable pageable);
    
    /**
     * 查询失败的登录日志
     */
    Page<LoginLog> findBySuccessFalse(Pageable pageable);
    
    /**
     * 按时间范围查询登录日志
     */
    List<LoginLog> findByLoginTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询用户最近的登录记录
     */
    List<LoginLog> findTop10ByUserIdOrderByLoginTimeDesc(Long userId);
}
