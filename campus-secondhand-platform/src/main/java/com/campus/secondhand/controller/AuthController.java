package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.LoginDTO;
import com.campus.secondhand.dto.RegisterDTO;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.repository.UserRepository;
import com.campus.secondhand.service.LoginLogService;
import com.campus.secondhand.util.JwtUtil;
import com.campus.secondhand.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private LoginLogService loginLogService;
    
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO registerDTO) {
        // 验证密码强度
        PasswordUtil.PasswordValidationResult passwordResult = PasswordUtil.validate(registerDTO.getPassword());
        if (!passwordResult.isValid()) {
            return Result.error(passwordResult.getMessage());
        }
        
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return Result.error("用户名已存在");
        }
        
        if (registerDTO.getPhone() != null && !registerDTO.getPhone().isEmpty() 
            && userRepository.existsByPhone(registerDTO.getPhone())) {
            return Result.error("手机号已存在");
        }
        
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        // 使用 BCrypt 加密密码
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setRole(0);
        user.setStatus(1);
        
        userRepository.save(user);
        
        logger.info("用户注册成功：username={}, userId={}", user.getUsername(), user.getId());
        
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
            logger.warn("登录失败 - 用户不存在：username={}", loginDTO.getUsername());
            // 记录失败日志（使用 0 作为默认 userId）
            loginLogService.recordLogin(0L, loginDTO.getUsername(), false, "用户不存在");
            return Result.error("用户不存在");
        }
        
        if (user.getStatus() != 1) {
            logger.warn("登录失败 - 账号已被禁用：username={}", user.getUsername());
            // 记录失败日志
            loginLogService.recordLogin(user.getId(), user.getUsername(), false, "账号已被禁用");
            return Result.error("账号已被禁用");
        }
        
        // 检查密码格式并验证
        boolean passwordValid = false;
        
        if (user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$")) {
            // BCrypt 加密密码，使用 matches 验证
            passwordValid = PasswordUtil.matches(loginDTO.getPassword(), user.getPassword());
        } else {
            // 明文密码，直接比较并自动升级
            if (loginDTO.getPassword().equals(user.getPassword())) {
                passwordValid = true;
                // 自动升级为 BCrypt 加密
                try {
                    String encryptedPassword = PasswordUtil.encode(user.getPassword());
                    user.setPassword(encryptedPassword);
                    userRepository.save(user);
                    logger.info("密码已自动升级为 BCrypt 格式：username={}", user.getUsername());
                } catch (Exception e) {
                    logger.error("密码升级失败：{}", e.getMessage(), e);
                }
            } else {
                passwordValid = false;
            }
        }
        
        if (!passwordValid) {
            logger.warn("登录失败 - 密码错误：username={}", user.getUsername());
            // 记录失败日志
            loginLogService.recordLogin(user.getId(), user.getUsername(), false, "密码错误");
            return Result.error("密码错误");
        }
        
        logger.info("用户登录成功：username={}, userId={}", user.getUsername(), user.getId());
        // 记录成功日志
        loginLogService.recordLogin(user.getId(), user.getUsername(), true, null);
        
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
