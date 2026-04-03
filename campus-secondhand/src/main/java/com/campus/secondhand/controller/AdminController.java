package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/users")
    public Result<?> getUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<User> userPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getUsername().contains(keyword) || 
                            (u.getNickname() != null && u.getNickname().contains(keyword)) ||
                            (u.getPhone() != null && u.getPhone().contains(keyword)))
                .toList();
            
            int start = Math.min(page * size, users.size());
            int end = Math.min((page + 1) * size, users.size());
            
            Map<String, Object> data = new HashMap<>();
            data.put("content", users.subList(start, end));
            data.put("totalElements", users.size());
            data.put("totalPages", (int) Math.ceil((double) users.size() / size));
            data.put("number", page);
            data.put("size", size);
            
            return Result.success(data);
        } else {
            userPage = userRepository.findAll(pageable);
            return Result.success(userPage);
        }
    }
    
    @PutMapping("/users/{id}/status")
    public Result<?> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        user.setStatus(status);
        userRepository.save(user);
        
        return Result.success("状态更新成功");
    }
    
    @GetMapping("/products")
    public Result<?> getProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Product> productPage;
        
        if (status != null) {
            productPage = productRepository.findByStatus(status, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        
        return Result.success(productPage);
    }
    
    @DeleteMapping("/products/{id}")
    public Result<?> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        product.setStatus(0);
        productRepository.save(product);
        
        return Result.success("删除成功");
    }
    
    @GetMapping("/stats")
    public Result<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long userCount = userRepository.count();
        long productCount = productRepository.count();
        long activeProductCount = productRepository.findByStatusIn(List.of(1)).size();
        long soldProductCount = productRepository.findByStatusIn(List.of(3)).size();
        
        stats.put("userCount", userCount);
        stats.put("productCount", productCount);
        stats.put("activeProductCount", activeProductCount);
        stats.put("soldProductCount", soldProductCount);
        
        return Result.success(stats);
    }
}
