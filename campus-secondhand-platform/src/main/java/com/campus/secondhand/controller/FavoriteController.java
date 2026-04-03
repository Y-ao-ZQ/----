package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Favorite;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.repository.FavoriteRepository;
import com.campus.secondhand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@CrossOrigin
public class FavoriteController {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping
    public Result<?> getFavorites(@RequestAttribute("userId") Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return Result.success(favorites);
    }
    
    @PostMapping("/{productId}")
    public Result<?> addFavorite(
            @PathVariable Long productId,
            @RequestAttribute("userId") Long userId
    ) {
        boolean exists = favoriteRepository.existsByUserIdAndProductId(userId, productId);
        if (exists) {
            return Result.error("已收藏");
        }
        
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        
        favoriteRepository.save(favorite);
        
        return Result.success("收藏成功");
    }
    
    @DeleteMapping("/{productId}")
    public Result<?> removeFavorite(
            @PathVariable Long productId,
            @RequestAttribute("userId") Long userId
    ) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
        return Result.success("取消收藏成功");
    }
    
    @GetMapping("/check/{productId}")
    public Result<?> checkFavorite(
            @PathVariable Long productId,
            @RequestAttribute("userId") Long userId
    ) {
        boolean exists = favoriteRepository.existsByUserIdAndProductId(userId, productId);
        return Result.success(exists);
    }
}