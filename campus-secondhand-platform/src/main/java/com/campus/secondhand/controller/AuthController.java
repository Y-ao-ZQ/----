package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.LoginDTO;
import com.campus.secondhand.dto.RegisterDTO;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.UserRepository;
import com.campus.secondhand.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return Result.error("用户名已存在");
        }
        
        if (registerDTO.getPhone() != null && !registerDTO.getPhone().isEmpty() 
            && userRepository.existsByPhone(registerDTO.getPhone())) {
            return Result.error("手机号已存在");
        }
        
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setRole(0);
        user.setStatus(1);
        
        userRepository.save(user);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("token", jwtUtil.generateToken(user.getId(), user.getUsername()));
        
        return Result.success("注册成功", data);
    }
    
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByUsernameOrPhoneOrEmail(loginDTO.getUsername())
                .orElse(null);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }
        
        if (user.getStatus() != 1) {
            return Result.error("账号已被禁用");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("avatar", user.getAvatar());
        data.put("role", user.getRole());
        data.put("token", jwtUtil.generateToken(user.getId(), user.getUsername()));
        
        return Result.success("登录成功", data);
    }
    
    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestAttribute("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        return Result.success(user);
    }
    
    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出登录成功");
    }
}
