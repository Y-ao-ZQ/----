package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.LoginLog;
import com.campus.secondhand.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/logs")
@CrossOrigin
public class AdminLogController {
    
    @Autowired
    private LoginLogService loginLogService;
    
    /**
     * 获取用户的登录日志
     */
    @GetMapping("/login/user/{userId}")
    public Result<?> getUserLoginLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<LoginLog> logs = loginLogService.getUserLoginLogs(userId, page, size);
        return Result.success(logs);
    }
    
    /**
     * 获取失败的登录日志
     */
    @GetMapping("/login/failed")
    public Result<?> getFailedLoginLogs(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        Page<LoginLog> logs = loginLogService.getFailedLogins(page, size);
        return Result.success(logs);
    }
    
    /**
     * 获取用户最近的登录记录
     */
    @GetMapping("/login/recent/{userId}")
    public Result<?> getRecentLoginLogs(@PathVariable Long userId) {
        return Result.success(loginLogService.getRecentLogins(userId));
    }
}
