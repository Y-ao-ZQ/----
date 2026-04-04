package com.campus.secondhand.service;

import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.ProductDeleteLog;
import com.campus.secondhand.repository.ProductDeleteLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 商品删除日志服务测试
 */
@DisplayName("商品删除日志服务测试")
class ProductDeleteLogServiceTest {
    
    @Mock
    private ProductDeleteLogRepository deleteLogRepository;
    
    @InjectMocks
    private ProductDeleteLogService deleteLogService;
    
    private MockHttpServletRequest request;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mozilla/5.0");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    
    @Test
    @DisplayName("测试记录删除日志 - 成功场景")
    void testRecordDeleteLog_Success() {
        // 准备测试数据
        Product product = new Product();
        product.setId(1L);
        product.setTitle("测试商品");
        product.setDescription("测试描述");
        product.setPrice(new BigDecimal("99.00"));
        product.setCategory("数码产品");
        product.setImages("image1.jpg,image2.jpg");
        product.setStatus(1);
        
        Long operatorId = 100L;
        String operatorName = "testuser";
        String deleteType = "USER";
        String reason = "不再需要";
        
        // 执行记录日志
        deleteLogService.recordDeleteLog(product, operatorId, operatorName, deleteType, reason);
        
        // 验证 Repository 被调用
        verify(deleteLogRepository, times(1)).save(any(ProductDeleteLog.class));
    }
    
    @Test
    @DisplayName("测试记录删除日志 - Repository 异常")
    void testRecordDeleteLog_RepositoryFails() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("测试商品");
        
        doThrow(new RuntimeException("数据库异常"))
            .when(deleteLogRepository).save(any(ProductDeleteLog.class));
        
        // 不应该抛出异常
        assertDoesNotThrow(() -> {
            deleteLogService.recordDeleteLog(product, 100L, "testuser", "USER", "测试原因");
        });
        
        verify(deleteLogRepository, times(1)).save(any(ProductDeleteLog.class));
    }
    
    @Test
    @DisplayName("测试获取商品删除历史")
    void testGetProductDeleteHistory() {
        Long productId = 1L;
        
        ProductDeleteLog log1 = new ProductDeleteLog();
        log1.setId(1L);
        log1.setProductId(productId);
        
        ProductDeleteLog log2 = new ProductDeleteLog();
        log2.setId(2L);
        log2.setProductId(productId);
        
        when(deleteLogRepository.findByProductId(productId))
            .thenReturn(Arrays.asList(log1, log2));
        
        List<ProductDeleteLog> logs = deleteLogService.getProductDeleteHistory(productId);
        
        assertNotNull(logs);
        assertEquals(2, logs.size());
        assertEquals(productId, logs.get(0).getProductId());
        
        verify(deleteLogRepository, times(1)).findByProductId(productId);
    }
    
    @Test
    @DisplayName("测试获取操作人的删除记录")
    void testGetOperatorDeleteLogs() {
        Long operatorId = 100L;
        
        when(deleteLogRepository.findByOperatorId(eq(operatorId), any()))
            .thenReturn(mock(org.springframework.data.domain.Page.class));
        
        var result = deleteLogService.getOperatorDeleteLogs(operatorId, 0, 10);
        
        assertNotNull(result);
        verify(deleteLogRepository, times(1)).findByOperatorId(eq(operatorId), any());
    }
    
    @Test
    @DisplayName("测试获取最近的删除记录")
    void testGetRecentDeleteLogs() {
        when(deleteLogRepository.findTop20ByOrderByDeleteTimeDesc())
            .thenReturn(Arrays.asList(new ProductDeleteLog()));
        
        List<ProductDeleteLog> logs = deleteLogService.getRecentDeleteLogs(20);
        
        assertNotNull(logs);
        assertEquals(1, logs.size());
        
        verify(deleteLogRepository, times(1)).findTop20ByOrderByDeleteTimeDesc();
    }
}
