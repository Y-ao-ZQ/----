package com.campus.secondhand.service;

import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.ProductDeleteLog;
import com.campus.secondhand.repository.ProductDeleteLogRepository;
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

/**
 * 商品删除日志服务
 */
@Service
public class ProductDeleteLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductDeleteLogService.class);
    
    @Autowired
    private ProductDeleteLogRepository deleteLogRepository;
    
    /**
     * 记录删除日志
     * 
     * @param product 被删除的商品
     * @param operatorId 操作人 ID
     * @param operatorName 操作人姓名
     * @param deleteType 删除类型
     * @param reason 删除原因
     */
    @Transactional
    public void recordDeleteLog(Product product, Long operatorId, String operatorName, 
                                String deleteType, String reason) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            
            ProductDeleteLog log = new ProductDeleteLog();
            log.setProductId(product.getId());
            log.setProductTitle(product.getTitle());
            log.setProductDescription(product.getDescription());
            log.setProductPrice(product.getPrice().toString());
            log.setProductCategory(product.getCategory());
            log.setProductImages(product.getImages());
            log.setProductStatus(product.getStatus());
            log.setOperatorId(operatorId);
            log.setOperatorName(operatorName);
            log.setDeleteReason(reason);
            log.setDeleteType(deleteType);
            log.setIpAddress(getIpAddress(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            
            deleteLogRepository.save(log);
            
            logger.info("商品删除日志已记录：productId={}, operator={}, type={}", 
                product.getId(), operatorName, deleteType);
            
        } catch (Exception e) {
            logger.error("记录商品删除日志失败：productId={}, error={}", 
                product.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 获取商品的删除历史
     */
    public List<ProductDeleteLog> getProductDeleteHistory(Long productId) {
        return deleteLogRepository.findByProductId(productId);
    }
    
    /**
     * 获取操作人的删除记录
     */
    public Page<ProductDeleteLog> getOperatorDeleteLogs(Long operatorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "deleteTime");
        return deleteLogRepository.findByOperatorId(operatorId, pageable);
    }
    
    /**
     * 获取最近的删除记录
     */
    public List<ProductDeleteLog> getRecentDeleteLogs(int limit) {
        return deleteLogRepository.findTop20ByOrderByDeleteTimeDesc();
    }
    
    /**
     * 统计删除数量
     */
    public Long countDeletesInPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return deleteLogRepository.countByDeleteTimeBetween(startTime, endTime);
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
        
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        
        return ip;
    }
}
