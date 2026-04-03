package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Message;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.MessageRepository;
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
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @PostMapping
    public Result<?> sendMessage(
            @RequestBody Map<String, Object> params,
            @RequestAttribute("userId") Long userId
    ) {
        Long receiverId = Long.valueOf(params.get("receiverId").toString());
        String content = params.get("content").toString();
        Long productId = params.get("productId") != null ? 
            Long.valueOf(params.get("productId").toString()) : null;
        
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (receiver == null) {
            return Result.error("接收者不存在");
        }
        
        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId).orElse(null);
        }
        
        User sender = userRepository.findById(userId).orElse(null);
        
        Message message = new Message();
        message.setSenderId(userId);
        message.setSenderName(sender.getNickname() != null ? sender.getNickname() : sender.getUsername());
        message.setReceiverId(receiverId);
        message.setReceiverName(receiver.getNickname() != null ? receiver.getNickname() : receiver.getUsername());
        message.setContent(content);
        message.setProductId(productId);
        message.setProductTitle(product != null ? product.getTitle() : null);
        message.setStatus(0);
        
        messageRepository.save(message);
        
        return Result.success("发送成功");
    }
    
    @GetMapping("/received")
    public Result<?> getReceivedMessages(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Message> messagePage = messageRepository.findByReceiverId(userId, pageable);
        
        return Result.success(messagePage);
    }
    
    @GetMapping("/sent")
    public Result<?> getSentMessages(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createTime");
        Page<Message> messagePage = messageRepository.findBySenderIdAndReceiverId(
            userId, userId, pageable);
        
        return Result.success(messagePage);
    }
    
    @GetMapping("/conversation/{userId}")
    public Result<?> getConversation(
            @PathVariable Long userId,
            @RequestAttribute("userId") Long currentUserId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createTime");
        Page<Message> messagePage = messageRepository.findBySenderIdAndReceiverId(
            currentUserId, userId, pageable);
        
        return Result.success(messagePage);
    }
    
    @PutMapping("/read/{messageId}")
    public Result<?> markAsRead(
            @PathVariable Long messageId,
            @RequestAttribute("userId") Long userId
    ) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return Result.error("消息不存在");
        }
        
        if (!message.getReceiverId().equals(userId)) {
            return Result.error("无权限操作");
        }
        
        message.setStatus(1);
        messageRepository.save(message);
        
        return Result.success("已标记为已读");
    }
    
    @GetMapping("/unread/count")
    public Result<?> getUnreadCount(@RequestAttribute("userId") Long userId) {
        Long count = messageRepository.countByReceiverIdAndStatus(userId, 0);
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.success(data);
    }
}
