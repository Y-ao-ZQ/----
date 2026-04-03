package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestAttribute("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        return Result.success(user);
    }
    
    @PutMapping("/info")
    public Result<?> updateUserInfo(
            @RequestBody Map<String, Object> params,
            @RequestAttribute("userId") Long userId
    ) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (params.get("nickname") != null) {
            user.setNickname(params.get("nickname").toString());
        }
        if (params.get("phone") != null) {
            String phone = params.get("phone").toString();
            if (!phone.isEmpty() && !user.getPhone().equals(phone) && userRepository.existsByPhone(phone)) {
                return Result.error("手机号已被使用");
            }
            user.setPhone(phone);
        }
        if (params.get("email") != null) {
            String email = params.get("email").toString();
            if (!email.isEmpty() && !user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                return Result.error("邮箱已被使用");
            }
            user.setEmail(email);
        }
        if (params.get("gender") != null) {
            user.setGender(params.get("gender").toString());
        }
        if (params.get("school") != null) {
            user.setSchool(params.get("school").toString());
        }
        if (params.get("college") != null) {
            user.setCollege(params.get("college").toString());
        }
        if (params.get("grade") != null) {
            user.setGrade(params.get("grade").toString());
        }
        if (params.get("avatar") != null) {
            user.setAvatar(params.get("avatar").toString());
        }
        
        userRepository.save(user);
        
        return Result.success("更新成功", user);
    }
    
    @PutMapping("/password")
    public Result<?> updatePassword(
            @RequestBody Map<String, String> params,
            @RequestAttribute("userId") Long userId
    ) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!user.getPassword().equals(oldPassword)) {
            return Result.error("原密码错误");
        }
        
        user.setPassword(newPassword);
        userRepository.save(user);
        
        return Result.success("密码修改成功");
    }
}
