package com.campus.secondhand.service;

import com.campus.secondhand.entity.LoginLog;
import com.campus.secondhand.repository.LoginLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginLogService.class);
    
    @Autowired
    private LoginLogRepository loginLogRepository;
    
    /**
     * 记录登录日志
     */
    @Transactional
    public void recordLogin(Long userId, String username, boolean success, String failureReason) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            
            LoginLog loginLog = new LoginLog();
            loginLog.setUserId(userId);
            loginLog.setUsername(username);
            loginLog.setSuccess(success);
            loginLog.setIpAddress(getIpAddress(request));
            loginLog.setUserAgent(request.getHeader("User-Agent"));
            loginLog.setFailureReason(failureReason);
            
            loginLogRepository.save(loginLog);
            
            if (!success) {
                logger.warn("登录失败记录 - username={}, ip={}, reason={}", 
                    username, getIpAddress(request), failureReason);
            }
            
        } catch (Exception e) {
            logger.error("记录登录日志失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户的登录日志
     */
    public Page<LoginLog> getUserLoginLogs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "loginTime");
        return loginLogRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 获取失败的登录日志
     */
    public Page<LoginLog> getFailedLogins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "loginTime");
        return loginLogRepository.findBySuccessFalse(pageable);
    }
    
    /**
     * 获取用户最近的登录记录
     */
    public List<LoginLog> getRecentLogins(Long userId) {
        return loginLogRepository.findTop10ByUserIdOrderByLoginTimeDesc(userId);
    }
    
    /**
     * 获取 IP 地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 如果是多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        
        return ip;
    }
}
