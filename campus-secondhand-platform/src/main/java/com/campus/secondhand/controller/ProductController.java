package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.ProductDTO;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.ProductRepository;
import com.campus.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
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
        Page<Product> productPage = productRepository.findBySellerId(userId, pageable);
        
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
    
    @DeleteMapping("/{id}")
    public Result<?> deleteProduct(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        if (!product.getSellerId().equals(userId)) {
            return Result.error("无权限删除此商品");
        }
        
        product.setStatus(0);
        productRepository.save(product);
        
        return Result.success("删除成功");
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
}
