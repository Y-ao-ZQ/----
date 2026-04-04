package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.ProductDTO;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.ProductDeleteLog;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.ProductRepository;
import com.campus.secondhand.repository.UserRepository;
import com.campus.secondhand.service.ProductDeleteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductDeleteLogService deleteLogService;
    
    @GetMapping
    public Result<?> getProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Product> productPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            productPage = productRepository.search(keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            productPage = productRepository.findByStatusAndCategory(1, category, pageable);
        } else {
            productPage = productRepository.findByStatus(1, pageable);
        }
        
        return Result.success(productPage);
    }
    
    @GetMapping("/{id}")
    public Result<?> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        
        return Result.success(product);
    }
    
    @GetMapping("/my")
    public Result<?> getMyProducts(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        // 查询未删除的商品
        Page<Product> productPage = productRepository.findBySellerIdAndNotDeleted(userId, pageable);
        
        return Result.success(productPage);
    }
    
    @PostMapping
    public Result<?> createProduct(@RequestBody ProductDTO productDTO, @RequestAttribute("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setCondition(productDTO.getCondition());
        product.setImages(productDTO.getImages());
        product.setContact(productDTO.getContact());
        product.setLocation(productDTO.getLocation());
        product.setSellerId(userId);
        product.setSellerName(user.getNickname() != null ? user.getNickname() : user.getUsername());
        product.setStatus(1);
        
        productRepository.save(product);
        
        return Result.success("发布成功", product);
    }
    
    @PutMapping("/{id}")
    public Result<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO,
            @RequestAttribute("userId") Long userId
    ) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        if (!product.getSellerId().equals(userId)) {
            return Result.error("无权限修改此商品");
        }
        
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setCondition(productDTO.getCondition());
        product.setImages(productDTO.getImages());
        product.setContact(productDTO.getContact());
        product.setLocation(productDTO.getLocation());
        
        productRepository.save(product);
        
        return Result.success("修改成功", product);
    }
    
    /**
     * 删除商品（逻辑删除）
     * 功能说明：
     * 1. 权限验证：只有卖家本人可以删除
     * 2. 数据完整性检查：检查商品是否存在、是否已删除
     * 3. 事务处理：确保删除操作和日志记录的一致性
     * 4. 日志记录：详细记录删除操作的所有信息
     * 
     * @param id 商品 ID
     * @param userId 当前用户 ID（从 JWT token 中提取）
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteProduct(
            @PathVariable Long id, 
            @RequestAttribute("userId") Long userId,
            @RequestParam(required = false) String reason) {
        
        logger.info("收到删除商品请求：productId={}, userId={}, reason={}", id, userId, reason);
        
        try {
            // 1. 数据完整性检查 - 检查商品是否存在
            Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
            
            // 2. 检查商品是否已被删除
            if (Boolean.TRUE.equals(product.getDeleted())) {
                logger.warn("商品已被删除：productId={}", id);
                return Result.error("该商品已被删除，无需重复操作");
            }
            
            // 3. 权限验证 - 验证是否为卖家本人
            if (!product.getSellerId().equals(userId)) {
                logger.warn("删除失败 - 无权限：productId={}, operatorId={}, sellerId={}", 
                    id, userId, product.getSellerId());
                return Result.error("无权限删除此商品，只有卖家本人可以删除");
            }
            
            // 4. 保存删除前的商品快照信息
            String productSnapshot = String.format(
                "商品快照 - ID: %d, 标题: %s, 价格: %.2f, 状态: %d, 浏览量: %d",
                product.getId(), 
                product.getTitle(),
                product.getPrice(),
                product.getStatus(),
                product.getViewCount()
            );
            logger.info("删除前商品状态：{}", productSnapshot);
            
            // 5. 执行逻辑删除
            product.setDeleted(true);
            product.setDeleteTime(LocalDateTime.now());
            productRepository.save(product);
            
            logger.info("商品已标记为删除：productId={}, title={}", product.getId(), product.getTitle());
            
            // 6. 获取操作人信息（用户名）
            String operatorName = "user_" + userId;
            try {
                User operator = userRepository.findById(userId).orElse(null);
                if (operator != null) {
                    operatorName = operator.getUsername();
                }
            } catch (Exception e) {
                logger.warn("获取操作人信息失败：{}", e.getMessage());
            }
            
            // 7. 记录删除日志（异步，不影响主事务）
            try {
                deleteLogService.recordDeleteLog(
                    product,
                    userId,
                    operatorName,
                    "USER", // 删除类型：用户主动删除
                    reason != null ? reason : "用户主动删除"
                );
            } catch (Exception e) {
                // 日志记录失败不影响删除操作，但需要记录错误
                logger.error("记录删除日志失败：productId={}, error={}", id, e.getMessage(), e);
            }
            
            // 8. 返回成功结果
            logger.info("商品删除成功：productId={}, operator={}", id, operatorName);
            return Result.success("商品已删除", product);
            
        } catch (IllegalArgumentException e) {
            // 业务异常（商品不存在、无权限等）
            logger.warn("删除失败 - 业务异常：productId={}, message={}", id, e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            // 系统异常，事务会自动回滚
            logger.error("删除失败 - 系统异常：productId={}, userId={}, error={}", 
                id, userId, e.getMessage(), e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<?> updateProductStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestAttribute("userId") Long userId
    ) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        if (!product.getSellerId().equals(userId)) {
            return Result.error("无权限操作此商品");
        }
        
        product.setStatus(status);
        productRepository.save(product);
        
        return Result.success("状态更新成功");
    }
    
    @GetMapping("/categories")
    public Result<?> getCategories() {
        List<String> categories = List.of(
            "数码产品",
            "图书教材",
            "生活用品",
            "运动户外",
            "服饰鞋包",
            "美妆护肤",
            "其他"
        );
        
        return Result.success(categories);
    }
    
    /**
     * 查看商品的删除历史（管理员功能）
     */
    @GetMapping("/delete-logs/{productId}")
    public Result<?> getProductDeleteLogs(@PathVariable Long productId) {
        try {
            List<ProductDeleteLog> logs = deleteLogService.getProductDeleteHistory(productId);
            return Result.success(logs);
        } catch (Exception e) {
            logger.error("查询删除日志失败：productId={}, error={}", productId, e.getMessage(), e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
