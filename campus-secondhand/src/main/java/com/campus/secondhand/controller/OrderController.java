package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.OrderRepository;
import com.campus.secondhand.repository.ProductRepository;
import com.campus.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping
    public Result<?> createOrder(
            @RequestBody Map<String, Object> params,
            @RequestAttribute("userId") Long userId
    ) {
        Long productId = Long.valueOf(params.get("productId").toString());
        String remark = params.get("remark") != null ? params.get("remark").toString() : null;
        
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        if (product.getSellerId().equals(userId)) {
            return Result.error("不能购买自己的商品");
        }
        
        if (product.getStatus() != 1) {
            return Result.error("商品已下架或已售出");
        }
        
        User buyer = userRepository.findById(userId).orElse(null);
        User seller = userRepository.findById(product.getSellerId()).orElse(null);
        
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        order.setProductId(productId);
        order.setProductTitle(product.getTitle());
        order.setBuyerId(userId);
        order.setBuyerName(buyer.getNickname() != null ? buyer.getNickname() : buyer.getUsername());
        order.setSellerId(product.getSellerId());
        order.setSellerName(product.getSellerName());
        order.setPrice(product.getPrice());
        order.setStatus(0);
        order.setRemark(remark);
        
        orderRepository.save(order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", order.getId());
        data.put("orderNo", order.getOrderNo());
        
        return Result.success("下单成功", data);
    }
    
    @GetMapping("/buy")
    public Result<?> getBuyOrders(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Order> orderPage = orderRepository.findByBuyerId(userId, pageable);
        
        return Result.success(orderPage);
    }
    
    @GetMapping("/sell")
    public Result<?> getSellOrders(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Order> orderPage = orderRepository.findBySellerId(userId, pageable);
        
        return Result.success(orderPage);
    }
    
    @GetMapping("/{id}")
    public Result<?> getOrderById(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            return Result.error("无权限查看此订单");
        }
        
        return Result.success(order);
    }
    
    @PutMapping("/{id}/pay")
    public Result<?> payOrder(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId
    ) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getBuyerId().equals(userId)) {
            return Result.error("无权限操作此订单");
        }
        
        if (order.getStatus() != 0) {
            return Result.error("订单状态异常");
        }
        
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        
        Product product = productRepository.findById(order.getProductId()).orElse(null);
        if (product != null) {
            product.setStatus(3);
            productRepository.save(product);
        }
        
        orderRepository.save(order);
        
        return Result.success("支付成功");
    }
    
    @PutMapping("/{id}/finish")
    public Result<?> finishOrder(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId
    ) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getSellerId().equals(userId)) {
            return Result.error("无权限操作此订单");
        }
        
        if (order.getStatus() != 1) {
            return Result.error("订单状态异常");
        }
        
        order.setStatus(2);
        order.setFinishTime(LocalDateTime.now());
        
        orderRepository.save(order);
        
        return Result.success("订单已完成");
    }
    
    @PutMapping("/{id}/cancel")
    public Result<?> cancelOrder(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId
    ) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getBuyerId().equals(userId)) {
            return Result.error("无权限操作此订单");
        }
        
        if (order.getStatus() != 0) {
            return Result.error("订单状态异常");
        }
        
        order.setStatus(-1);
        
        Product product = productRepository.findById(order.getProductId()).orElse(null);
        if (product != null) {
            product.setStatus(1);
            productRepository.save(product);
        }
        
        orderRepository.save(order);
        
        return Result.success("订单已取消");
    }
}
