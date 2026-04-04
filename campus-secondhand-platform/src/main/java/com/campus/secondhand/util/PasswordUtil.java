package com.campus.secondhand.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查密码强度
     * @param password 密码
     * @return 密码强度等级：WEAK(弱), MEDIUM(中), STRONG(强)
     */
    public static PasswordStrength checkStrength(String password) {
        if (password == null || password.length() < 6) {
            return PasswordStrength.WEAK;
        }
        
        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else hasSpecial = true;
        }
        
        int strength = (hasDigit ? 1 : 0) + (hasLower ? 1 : 0) + 
                       (hasUpper ? 1 : 0) + (hasSpecial ? 1 : 0);
        
        if (password.length() >= 12 && strength >= 3) {
            return PasswordStrength.STRONG;
        } else if (password.length() >= 8 && strength >= 2) {
            return PasswordStrength.MEDIUM;
        } else {
            return PasswordStrength.WEAK;
        }
    }
    
    /**
     * 验证密码是否符合要求
     * @param password 密码
     * @return 验证结果
     */
    public static PasswordValidationResult validate(String password) {
        if (password == null || password.isEmpty()) {
            return new PasswordValidationResult(false, "密码不能为空");
        }
        
        if (password.length() < 6) {
            return new PasswordValidationResult(false, "密码长度至少为 6 位");
        }
        
        if (password.length() > 32) {
            return new PasswordValidationResult(false, "密码长度不能超过 32 位");
        }
        
        boolean hasDigit = false;
        boolean hasLetter = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLetter(c)) hasLetter = true;
        }
        
        if (!hasDigit || !hasLetter) {
            return new PasswordValidationResult(false, "密码必须包含数字和字母");
        }
        
        return new PasswordValidationResult(true, "密码符合要求");
    }
    
    /**
     * 密码强度枚举
     */
    public enum PasswordStrength {
        WEAK("弱"),
        MEDIUM("中"),
        STRONG("强");
        
        private final String description;
        
        PasswordStrength(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 密码验证结果
     */
    public static class PasswordValidationResult {
        private final boolean valid;
        private final String message;
        
        public PasswordValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
