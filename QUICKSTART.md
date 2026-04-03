# 校园二手交易平台 - 快速开始指南

## 📦 项目已完成！

恭喜！校园二手交易平台已经开发完成。以下是项目的完整功能和使用说明。

## 🎯 项目概述

这是一个完整的校园二手交易平台，包含以下核心功能：

### 用户端功能
✅ 用户注册和登录（JWT 认证）
✅ 商品浏览和搜索
✅ 商品分类筛选
✅ 商品详情查看
✅ 发布商品
✅ 编辑/下架商品
✅ 收藏商品
✅ 创建订单
✅ 订单管理（付款/发货/取消）
✅ 消息中心
✅ 个人中心
✅ 密码修改

### 管理端功能
✅ 数据统计面板
✅ 用户管理（启用/禁用）
✅ 商品管理（下架）
✅ 销售统计

## 🚀 快速开始（3 步走）

### 第 1 步：安装 MySQL 数据库

**重要：** 项目需要 MySQL 数据库才能运行！

1. 下载 MySQL：https://dev.mysql.com/downloads/mysql/
2. 安装并设置密码（建议使用：root123456）
3. 创建数据库：
```sql
CREATE DATABASE campus_secondhand DEFAULT CHARACTER SET utf8mb4;
```

### 第 2 步：配置数据库连接

编辑文件：`campus-secondhand/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_secondhand
    username: root        # 你的 MySQL 用户名
    password: 你的密码     # 修改为你的 MySQL 密码
```

### 第 3 步：导入测试数据

在 MySQL 中执行：
```bash
mysql -u root -p campus_secondhand < campus-secondhand/src/main/resources/init-db.sql
```

或使用 MySQL Workbench 打开并执行 `init-db.sql` 文件。

## 🎮 启动项目

### 启动后端（2 种方式）

**方式 1：使用 Maven 命令**
```bash
cd campus-secondhand
mvn spring-boot:run
```

**方式 2：使用 IDE**
- 用 IntelliJ IDEA 或 Eclipse 打开 `campus-secondhand` 项目
- 运行 `CampusSecondhandApplication.java`

✅ 后端启动成功后访问：http://localhost:8080/api/products

### 启动前端

```bash
cd campus-secondhand-frontend
npm install    # 首次运行需要
npm run dev
```

✅ 前端启动成功后访问：http://localhost:3000

## 👥 测试账号

### 管理员账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |

### 学生账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| zhangsan | 123456 | 普通用户 |
| lisi | 123456 | 普通用户 |
| wangwu | 123456 | 普通用户 |
| zhaoliu | 123456 | 普通用户 |
| sunqi | 123456 | 普通用户 |

## 📱 功能演示流程

### 1. 作为买家
1. 访问 http://localhost:3000
2. 使用 `zhangsan/123456` 登录
3. 浏览首页商品列表
4. 点击商品查看详情
5. 点击"收藏"收藏商品
6. 点击"我想要"创建订单
7. 在"订单"页面确认付款
8. 在"消息"页面查看消息

### 2. 作为卖家
1. 使用 `lisi/123456` 登录
2. 点击"发布"发布新商品
3. 填写商品信息并提交
4. 在"我的"页面查看已发布商品
5. 在"订单"页面查看卖出订单
6. 确认发货

### 3. 作为管理员
1. 使用 `admin/admin123` 登录
2. 点击头像，选择"后台管理"
3. 查看平台数据统计
4. 管理用户（启用/禁用）
5. 管理商品（下架违规商品）

## 🗄️ 数据库表结构

项目包含 5 个核心数据表：

1. **users** - 用户表
2. **products** - 商品表
3. **orders** - 订单表
4. **messages** - 消息表
5. **favorites** - 收藏表

详细说明请查看 README.md

## 🔧 技术架构

### 后端技术栈
- Java 8 + Spring Boot 2.7.18
- Spring Data JPA（数据访问）
- MySQL 8.0（数据库）
- JWT（身份认证）
- Maven（构建工具）

### 前端技术栈
- Vue 3（框架）
- Vue Router（路由）
- Pinia（状态管理）
- Element Plus（UI 组件）
- Axios（HTTP 客户端）
- Vite（构建工具）

## 📂 项目结构

```
校园二手/
├── campus-secondhand/           # 后端项目
│   ├── src/main/java/
│   │   └── com/campus/secondhand/
│   │       ├── entity/          # 5 个实体类
│   │       ├── repository/      # 5 个 Repository 接口
│   │       ├── controller/      # 8 个 Controller
│   │       ├── config/          # 配置类
│   │       ├── interceptor/     # 认证拦截器
│   │       ├── util/            # JWT 工具类
│   │       └── dto/             # 数据传输对象
│   └── src/main/resources/
│       ├── application.yml      # 配置文件
│       └── init-db.sql          # 数据库初始化脚本
│
├── campus-secondhand-frontend/  # 前端项目
│   ├── src/
│   │   ├── views/               # 11 个页面组件
│   │   ├── components/          # 布局组件
│   │   ├── api/                 # API 接口封装
│   │   ├── stores/              # Pinia 状态管理
│   │   ├── router/              # 路由配置
│   │   └── utils/               # 工具类
│   ├── package.json
│   └── vite.config.js
│
├── README.md                    # 项目文档
├── DEPLOYMENT.md                # 部署指南
├── QUICKSTART.md                # 本文件
└── start.bat                    # Windows 启动脚本
```

## 🎨 页面列表

### 前端页面（11 个）
1. **首页（Home）** - 商品列表、分类筛选、搜索
2. **登录页（Login）** - 用户登录
3. **注册页（Register）** - 用户注册
4. **商品详情（ProductDetail）** - 商品详细信息
5. **发布商品（Publish）** - 发布新商品
6. **我的（My）** - 我发布的商品
7. **我的收藏（Favorites）** - 收藏的商品
8. **消息中心（Messages）** - 系统消息
9. **订单管理（Orders）** - 买入/卖出订单
10. **个人中心（Profile）** - 个人资料和密码修改
11. **后台管理（Admin）** - 管理员后台

## 📋 API 接口

### 认证接口（4 个）
- POST /api/auth/register - 注册
- POST /api/auth/login - 登录
- GET /api/auth/info - 获取用户信息
- POST /api/auth/logout - 退出

### 商品接口（7 个）
- GET /api/products - 商品列表
- GET /api/products/{id} - 商品详情
- POST /api/products - 发布商品
- PUT /api/products/{id} - 修改商品
- DELETE /api/products/{id} - 删除商品
- GET /api/products/my - 我的商品
- GET /api/products/categories - 商品分类

### 订单接口（5 个）
- POST /api/orders - 创建订单
- GET /api/orders/buy - 买入订单
- GET /api/orders/sell - 卖出订单
- PUT /api/orders/{id}/pay - 确认付款
- PUT /api/orders/{id}/finish - 确认发货

### 其他接口
- 收藏接口（4 个）
- 消息接口（4 个）
- 用户接口（3 个）
- 管理接口（5 个）

**总计：30+ 个 API 接口**

## ✅ 已完成功能清单

### 核心功能
- [x] 用户注册和登录
- [x] JWT Token 认证
- [x] 商品浏览和搜索
- [x] 商品分类筛选
- [x] 商品详情查看
- [x] 发布商品
- [x] 编辑商品
- [x] 上架/下架商品
- [x] 收藏功能
- [x] 订单创建和管理
- [x] 消息中心
- [x] 个人中心
- [x] 修改密码

### 管理功能
- [x] 数据统计
- [x] 用户管理
- [x] 商品管理
- [x] 用户启用/禁用
- [x] 商品下架

### 数据初始化
- [x] 数据库表结构
- [x] 管理员账号
- [x] 5 个学生账号
- [x] 12 个示例商品

## ⚠️ 注意事项

1. **数据库必须安装** - 项目依赖 MySQL 数据库
2. **密码配置** - 记得修改 application.yml 中的数据库密码
3. **端口占用** - 确保 8080 和 3000 端口未被占用
4. **跨域问题** - 项目已配置 CORS，无需额外设置

## 🐛 常见问题

### Q1: 数据库连接失败？
A: 检查 MySQL 服务是否启动，确认用户名密码正确

### Q2: 前端无法访问后端？
A: 确认后端已启动，访问 http://localhost:8080/api/products 测试

### Q3: 登录失败？
A: 确认数据库已导入测试数据，检查 users 表

### Q4: 页面空白？
A: 打开浏览器控制台查看错误，检查 npm 依赖是否安装

详细问题解决方案请查看 DEPLOYMENT.md

## 🎯 后续优化方向

如需进一步完善项目，可以考虑：

1. **图片上传** - 添加商品图片上传功能
2. **即时通讯** - 实现 WebSocket 实时聊天
3. **支付集成** - 接入支付宝/微信支付 API
4. **商品推荐** - 基于浏览记录的推荐算法
5. **移动端** - 开发微信小程序或 APP
6. **评价系统** - 买卖双方互评
7. **搜索优化** - 使用 Elasticsearch
8. **数据统计** - 完善数据分析图表

## 📞 需要帮助？

如遇到问题：
1. 查看 README.md 了解项目详情
2. 查看 DEPLOYMENT.md 获取部署指南
3. 检查后端日志（控制台输出）
4. 检查前端浏览器控制台（F12）

## 🎉 开始使用

现在，打开浏览器访问 http://localhost:3000 开始体验吧！

**祝你使用愉快！** 🚀
