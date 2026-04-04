# 安全功能测试指南

## 🎯 测试环境

- **前端地址**：http://localhost:3002
- **后端地址**：http://localhost:8080/api
- **数据库**：MySQL (campus_secondhand)

---

## 📝 测试场景

### 1. 密码强度验证测试

#### 弱密码测试
**测试步骤**：
1. 访问 http://localhost:3002/register
2. 输入用户名：testuser1
3. 输入密码：`123456`（仅数字）
4. 观察密码强度指示器

**预期结果**：
- ❌ 显示红色强度条（弱）
- ❌ 提交时提示"密码必须包含数字和字母"
- ❌ 注册失败

#### 中等密码测试
**测试步骤**：
1. 输入密码：`abc12345`（8 位，字母 + 数字）

**预期结果**：
- ⚠️ 显示橙色强度条（中）
- ✅ 允许注册

#### 强密码测试
**测试步骤**：
1. 输入密码：`Abc@123456`（12 位，大小写 + 数字 + 特殊字符）

**预期结果**：
- ✅ 显示绿色强度条（强）
- ✅ 允许注册
- ✅ 实时强度指示器显示"强"

---

### 2. 密码加密存储测试

**测试步骤**：
1. 使用强密码注册新用户：testuser2 / Abc@123456
2. 登录数据库查看用户表
3. 检查 password 字段

**预期结果**：
```sql
SELECT id, username, password FROM user WHERE username = 'testuser2';
```

密码应该是类似这样的 BCrypt 哈希：
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

**验证**：
- ❌ 不应该是：`Abc@123456`（明文）
- ✅ 应该是：`$2a$10$...`（BCrypt 哈希，60 字符）

---

### 3. 登录功能测试

#### 成功登录
**测试步骤**：
1. 访问 http://localhost:3002/login
2. 输入正确的用户名和密码
3. 点击登录

**预期结果**：
- ✅ 登录成功提示
- ✅ 跳转到首页
- ✅ 数据库 login_logs 表新增一条成功记录

#### 失败登录 - 密码错误
**测试步骤**：
1. 输入正确的用户名
2. 输入错误的密码

**预期结果**：
- ❌ 提示"密码错误"
- ✅ 数据库 login_logs 表新增一条失败记录
- ✅ 后端日志显示警告

**验证 SQL**：
```sql
SELECT * FROM login_logs 
WHERE username = 'testuser2' 
ORDER BY login_time DESC 
LIMIT 5;
```

#### 失败登录 - 用户不存在
**测试步骤**：
1. 输入不存在的用户名
2. 输入任意密码

**预期结果**：
- ❌ 提示"用户不存在"
- ✅ 数据库 login_logs 表新增一条失败记录（user_id=0）

---

### 4. 登录日志审计测试

**测试步骤**：
1. 使用管理员账户登录
2. 访问登录日志查询接口

**API 测试**：
```bash
# 查询用户的登录日志
GET http://localhost:8080/api/admin/logs/login/user/1

# 查询失败的登录日志
GET http://localhost:8080/api/admin/logs/login/failed?page=0&size=20

# 查询最近登录记录
GET http://localhost:8080/api/admin/logs/login/recent/1
```

**预期结果**：
- ✅ 返回 JSON 格式的登录日志
- ✅ 包含 IP 地址、User-Agent、登录时间等信息
- ✅ 失败日志包含失败原因

---

### 5. JWT Token 验证测试

#### 有效 Token 访问
**测试步骤**：
1. 登录获取 token
2. 使用 token 访问需要认证的接口

**API 测试**：
```bash
GET http://localhost:8080/api/products/my
Authorization: Bearer <your-token>
```

**预期结果**：
- ✅ 返回用户自己的商品列表
- ✅ 状态码 200

#### 无效 Token 访问
**测试步骤**：
1. 使用过期或篡改的 token

**API 测试**：
```bash
GET http://localhost:8080/api/products/my
Authorization: Bearer invalid-token
```

**预期结果**：
- ❌ 状态码 401
- ❌ 返回错误信息"token 已过期"或"未登录"

#### 无 Token 访问
**测试步骤**：
1. 不带 token 访问需要认证的接口

**API 测试**：
```bash
GET http://localhost:8080/api/products/my
```

**预期结果**：
- ❌ 状态码 401
- ❌ 返回"未登录或 token 已过期"

---

### 6. 前端安全测试

#### XSS 攻击测试
**测试步骤**：
1. 注册用户名：`<script>alert('XSS')</script>`
2. 昵称：`<img src=x onerror=alert('XSS')>`
3. 尝试在其他用户页面显示

**预期结果**：
- ✅ Vue 自动转义 HTML，脚本不执行
- ✅ 用户名显示为纯文本

#### SQL 注入测试
**测试步骤**：
1. 登录用户名：`admin' OR '1'='1`
2. 密码：任意

**预期结果**：
- ❌ 提示"用户不存在"
- ✅ JPA 参数化查询防止 SQL 注入

---

### 7. 并发登录测试

**测试步骤**：
1. 使用同一账户在多个浏览器同时登录
2. 观察登录日志

**预期结果**：
- ✅ 允许多设备同时登录
- ✅ 每次登录都有独立日志记录
- ✅ IP 地址、时间戳准确

---

## 🔍 数据库验证查询

### 查看登录日志统计
```sql
-- 总登录次数
SELECT COUNT(*) as total_logins FROM login_logs;

-- 成功/失败统计
SELECT 
    success,
    COUNT(*) as count
FROM login_logs
GROUP BY success;

-- 按用户统计
SELECT 
    username,
    COUNT(*) as total,
    SUM(CASE WHEN success THEN 1 ELSE 0 END) as success_count,
    SUM(CASE WHEN success THEN 0 ELSE 1 END) as fail_count
FROM login_logs
GROUP BY username
ORDER BY total DESC;

-- 最近 24 小时登录情况
SELECT * FROM login_logs
WHERE login_time >= DATE_SUB(NOW(), INTERVAL 1 DAY)
ORDER BY login_time DESC;
```

### 查看用户密码加密
```sql
-- 检查密码是否为 BCrypt 格式
SELECT 
    id,
    username,
    password,
    CASE 
        WHEN password LIKE '$2a$%' THEN 'BCrypt'
        WHEN LENGTH(password) < 60 THEN '明文（危险！）'
        ELSE '其他格式'
    END as password_type
FROM user;
```

---

## ✅ 测试检查清单

### 基础功能
- [ ] 密码强度验证工作正常
- [ ] 弱密码无法注册
- [ ] 密码加密存储（BCrypt 格式）
- [ ] 登录验证使用密码哈希比较
- [ ] 登录成功返回 JWT token

### 安全审计
- [ ] 登录成功记录日志
- [ ] 登录失败记录日志
- [ ] 日志包含 IP 地址
- [ ] 日志包含 User-Agent
- [ ] 日志包含失败原因

### JWT 认证
- [ ] 有效 token 可以访问受保护接口
- [ ] 无效 token 被拒绝
- [ ] 过期 token 被拒绝
- [ ] 无 token 被拒绝
- [ ] Token 包含正确的用户信息

### 前端安全
- [ ] XSS 攻击被阻止
- [ ] SQL 注入被阻止
- [ ] 密码强度实时反馈
- [ ] 错误信息不泄露敏感数据
- [ ] Token 安全存储和管理

---

## 🐛 问题排查

### 如果密码加密不工作
1. 检查 `PasswordUtil.java` 是否存在
2. 检查 `AuthController` 是否调用 `PasswordUtil.encode()`
3. 查看后端日志是否有错误

### 如果登录日志没有记录
1. 检查 `login_logs` 表是否创建
2. 检查 `LoginLogService` 是否注入成功
3. 查看后端日志

### 如果前端密码强度不显示
1. 打开浏览器开发者工具
2. 查看 Console 是否有 JavaScript 错误
3. 检查 Vue 组件是否正确导入

---

## 📊 性能基准

**预期性能指标**：
- 登录响应时间：< 500ms
- Token 验证时间：< 50ms
- 登录日志写入：< 100ms
- 密码加密时间：< 200ms（BCrypt 故意设计较慢）

**并发能力**：
- 同时登录：100 用户/秒
- Token 验证：1000 请求/秒
- 日志查询：50 请求/秒

---

**测试指南版本**：v1.0  
**最后更新**：2026-04-04  
**适用版本**：v1.1.0-security
