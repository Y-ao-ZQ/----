package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.ProductDeleteLog;
import com.campus.secondhand.repository.ProductRepository;
import com.campus.secondhand.service.ProductDeleteLogService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 商品删除功能单元测试
 */
@DisplayName("商品删除功能测试")
class ProductDeleteControllerTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ProductDeleteLogService deleteLogService;
    
    @InjectMocks
    private ProductController productController;
    
    private MockHttpServletRequest request;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    
    @Test
    @DisplayName("测试删除商品 - 成功场景")
    void testDeleteProduct_Success() {
        // 准备测试数据
        Long productId = 1L;
        Long userId = 100L;
        String reason = "不再需要此商品";
        
        Product product = new Product();
        product.setId(productId);
        product.setTitle("测试商品");
        product.setPrice(new BigDecimal("99.00"));
        product.setStatus(1);
        product.setSellerId(userId);
        product.setDeleted(false);
        
        // 模拟 Repository 行为
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
        // 执行删除操作
        request.setAttribute("userId", userId);
        Result<?> result = productController.deleteProduct(productId, userId, reason);
        
        // 验证结果
        assertEquals(200, result.getCode());
        assertEquals("商品已删除", result.getMessage());
        
        // 验证商品被标记为删除
        assertTrue(product.getDeleted());
        assertNotNull(product.getDeleteTime());
        
        // 验证 Repository 被调用
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
        
        // 验证日志服务被调用
        verify(deleteLogService, times(1)).recordDeleteLog(
            eq(product),
            eq(userId),
            anyString(),
            eq("USER"),
            eq(reason)
        );
    }
    
    @Test
    @DisplayName("测试删除商品 - 商品不存在")
    void testDeleteProduct_NotFound() {
        Long productId = 999L;
        Long userId = 100L;
        
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        request.setAttribute("userId", userId);
        Result<?> result = productController.deleteProduct(productId, userId, null);
        
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("商品不存在"));
        
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
        verify(deleteLogService, never()).recordDeleteLog(any(), any(), any(), any(), any());
    }
    
    @Test
    @DisplayName("测试删除商品 - 无权限删除")
    void testDeleteProduct_NoPermission() {
        Long productId = 1L;
        Long ownerId = 100L;
        Long operatorId = 200L; // 不同的用户
        
        Product product = new Product();
        product.setId(productId);
        product.setTitle("测试商品");
        product.setPrice(new BigDecimal("99.00"));
        product.setSellerId(ownerId);
        product.setDeleted(false);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        request.setAttribute("userId", operatorId);
        Result<?> result = productController.deleteProduct(productId, operatorId, null);
        
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("无权限删除此商品"));
        
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
        verify(deleteLogService, never()).recordDeleteLog(any(), any(), any(), any(), any());
    }
    
    @Test
    @DisplayName("测试删除商品 - 商品已被删除")
    void testDeleteProduct_AlreadyDeleted() {
        Long productId = 1L;
        Long userId = 100L;
        
        Product product = new Product();
        product.setId(productId);
        product.setTitle("测试商品");
        product.setPrice(new BigDecimal("99.00"));
        product.setSellerId(userId);
        product.setDeleted(true); // 已删除
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        request.setAttribute("userId", userId);
        Result<?> result = productController.deleteProduct(productId, userId, null);
        
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("该商品已被删除"));
        
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
        verify(deleteLogService, never()).recordDeleteLog(any(), any(), any(), any(), any());
    }
    
    @Test
    @DisplayName("测试删除商品 - 日志记录失败不影响删除操作")
    void testDeleteProduct_LogServiceFails() {
        Long productId = 1L;
        Long userId = 100L;
        
        Product product = new Product();
        product.setId(productId);
        product.setTitle("测试商品");
        product.setPrice(new BigDecimal("99.00"));
        product.setSellerId(userId);
        product.setDeleted(false);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        doThrow(new RuntimeException("日志服务异常"))
            .when(deleteLogService).recordDeleteLog(any(), any(), any(), any(), any());
        
        request.setAttribute("userId", userId);
        Result<?> result = productController.deleteProduct(productId, userId, null);
        
        // 删除应该成功，尽管日志记录失败
        assertEquals(200, result.getCode());
        assertEquals("商品已删除", result.getMessage());
        assertTrue(product.getDeleted());
        
        verify(productRepository, times(1)).save(product);
        verify(deleteLogService, times(1)).recordDeleteLog(any(), any(), any(), any(), any());
    }
    
    @Test
    @DisplayName("测试删除商品 - 系统异常处理")
    void testDeleteProduct_SystemException() {
        Long productId = 1L;
        Long userId = 100L;
        
        Product product = new Product();
        product.setId(productId);
        product.setTitle("测试商品");
        product.setPrice(new BigDecimal("99.00"));
        product.setSellerId(userId);
        product.setDeleted(false);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
            .thenThrow(new RuntimeException("数据库异常"));
        
        request.setAttribute("userId", userId);
        Result<?> result = productController.deleteProduct(productId, userId, null);
        
        // 删除应该失败
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("删除失败"));
        
        // 注意：在真实 Spring 环境中，事务会回滚，product.deleted 不会被设置
        // 但在单元测试中，由于没有 Spring 事务管理，product 对象可能已被修改
        // 实际的事务回滚测试应该在集成测试中进行
    }
}
