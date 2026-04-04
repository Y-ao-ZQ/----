# 🎨 校园二手交易平台

> 基于最新技术栈打造的现代化校园二手交易平台，集成 JWT 认证、密码加密、商品管理、交易流程等完整功能，提供安全可靠的校园交易解决方案。

<div align="center">

![Vue 3](https://img.shields.io/badge/Vue-3.4.21-4FC08D?style=flat-square&logo=vue.js)
![Vite](https://img.shields.io/badge/Vite-5.2.8-646CFF?style=flat-square&logo=vite)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-6DB33F?style=flat-square&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=openjdk)
![Element Plus](https://img.shields.io/badge/Element_Plus-2.6.3-409EFF?style=flat-square)
![JWT](https://img.shields.io/badge/JWT-0.12.3-000000?style=flat-square)

[特性](#-特性亮点) • [技术栈](#-技术栈) • [快速开始](#-快速开始) • [功能模块](#-功能模块) • [安全特性](#-安全特性) • [API 文档](#-api-文档) • [常见问题](#-常见问题)

</div>

***

## ✨ 特性亮点

### 🔐 安全可靠的认证系统

- **JWT Token 认证**：安全的无状态认证机制
- **密码加密存储**：BCrypt 强加密，支持密码强度验证
- **登录日志审计**：完整记录用户登录行为
- **Token 自动刷新**：无缝续期，提升用户体验
- **指纹验证**：增强 JWT 安全性，防止 Token 盗用

### 🛍️ 完整的商品管理

- **商品发布与编辑**：支持多图片上传
- **商品状态管理**：出售中、已卖出、已下架、离线状态
- **逻辑删除机制**：安全删除，支持数据恢复
- **删除日志审计**：完整记录删除操作，可追溯
- **分类筛选**：支持多类别商品管理

### 💼 交易流程管理

- **订单创建**：买家下单流程
- **支付确认**：卖家确认收款
- **发货确认**：买家确认收货
- **订单取消**：支持取消交易
- **订单追踪**：完整的订单状态流转

### 🎨 现代化 UI 设计

- **清新校园风**：翡翠绿 + 天空蓝渐变主题
- **玻璃态设计**：半透明背景 + 毛玻璃效果
- **流畅动画**：丰富的过渡和交互动画
- **响应式布局**：适配桌面、平板、手机

### 📊 数据统计与审计

- **登录日志**：记录用户登录时间、IP 等信息
- **删除日志**：商品删除操作完整审计
- **数据统计**：用户、商品、交易数据统计
- **操作追溯**：所有关键操作可追溯

***

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 17 | LTS 长期支持版 |
| **Spring Boot** | 3.2.0 | 最新稳定版 |
| **Spring Security** | 6.x | 安全框架 |
| **Spring Data JPA** | - | ORM 数据访问 |
| **MySQL** | 9.1.0+ | 关系型数据库 |
| **JWT** | 0.12.3 | JSON Web Token 认证 |
| **BCrypt** | - | 密码加密 |
| **Maven** | - | 项目构建管理 |
| **Lombok** | - | 简化代码 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| **Vue** | 3.4.21 | 渐进式 JavaScript 框架 |
| **Vite** | 5.2.8 | 下一代前端构建工具 |
| **Element Plus** | 2.6.3 | Vue 3 组件库 |
| **Pinia** | 2.1.7 | Vue 状态管理 |
| **Vue Router** | 4.3.0 | 官方路由管理器 |
| **Axios** | 1.6.8 | HTTP 客户端 |

***

## 📁 项目结构

```
校园二手/
├── 📂 campus-secondhand-platform/          # 后端项目 (Spring Boot 3.x + Java 17)
│   ├── 📂 src/main/java/com/campus/secondhand/
│   │   ├── 📂 config/                      # 配置类
│   │   │   ├── SecurityConfig.java         # 安全配置 (CORS, JWT)
│   │   │   └── WebConfig.java              # Web 配置
│   │   ├── 📂 controller/                  # 控制器层
│   │   │   ├── AuthController.java         # 认证接口
│   │   │   ├── ProductController.java      # 商品接口
│   │   │   ├── OrderController.java        # 订单接口
│   │   │   ├── FavoriteController.java     # 收藏接口
│   │   │   ├── MessageController.java      # 消息接口
│   │   │   ├── UserController.java         # 用户接口
│   │   │   └── AdminLogController.java     # 管理日志接口
│   │   ├── 📂 entity/                      # 实体类
│   │   │   ├── User.java                   # 用户实体
│   │   │   ├── Product.java                # 商品实体
│   │   │   ├── Order.java                  # 订单实体
│   │   │   ├── Message.java                # 消息实体
│   │   │   ├── Favorite.java               # 收藏实体
│   │   │   ├── LoginLog.java               # 登录日志实体
│   │   │   └── ProductDeleteLog.java       # 商品删除日志实体
│   │   ├── 📂 repository/                  # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   ├── MessageRepository.java
│   │   │   ├── FavoriteRepository.java
│   │   │   ├── LoginLogRepository.java
│   │   │   └── ProductDeleteLogRepository.java
│   │   ├── 📂 service/                     # 业务逻辑层
│   │   │   ├── AuthService.java
│   │   │   ├── ProductService.java
│   │   │   ├── OrderService.java
│   │   │   ├── LoginLogService.java        # 登录日志服务
│   │   │   └── ProductDeleteLogService.java # 删除日志服务
│   │   ├── 📂 util/                        # 工具类
│   │   │   ├── JwtUtil.java                # JWT 工具
│   │   │   ├── PasswordUtil.java           # 密码加密工具
│   │   │   └── Result.java                 # 统一返回结果
│   │   ├── 📂 vo/                          # 视图对象
│   │   └── 📂 dto/                         # 数据传输对象
│   ├── 📂 src/main/resources/
│   │   ├── application.yml                 # 配置文件
│   │   └── init-db.sql                     # 数据库初始化脚本
│   ├── 📂 src/test/                        # 测试代码
│   │   └── java/com/campus/secondhand/
│   │       └── controller/
│   │           ├── ProductDeleteControllerTest.java
│   │           └── ProductDeleteLogServiceTest.java
│   ├── 📂 database/                        # 数据库脚本
│   │   └── migrate_password.sql            # 密码迁移脚本
│   ├── 📄 pom.xml                          # Maven 依赖配置
│   ├── 📄 SECURITY_IMPLEMENTATION.md       # 安全实现文档
│   ├── 📄 DELETE_FEATURE_TEST_REPORT.md    # 删除功能测试报告
│   ├── 📄 TESTING_GUIDE.md                 # 测试指南
│   └── 📄 TEST_ACCOUNTS.md                 # 测试账号说明
│
├── 📂 campus-secondhand-platform-frontend/ # 前端项目 (Vue 3 + Vite)
│   ├── 📂 src/
│   │   ├── 📂 views/                       # 页面组件
│   │   │   ├── Home.vue                    # 首页
│   │   │   ├── Login.vue                   # 登录页
│   │   │   ├── Register.vue                # 注册页
│   │   │   ├── ProductDetail.vue           # 商品详情
│   │   │   ├── My.vue                      # 个人中心
│   │   │   ├── Publish.vue                 # 发布商品
│   │   │   ├── Orders.vue                  # 订单管理
│   │   │   ├── Messages.vue                # 消息中心
│   │   │   ├── Favorites.vue               # 收藏夹
│   │   │   ├── Admin.vue                   # 后台管理
│   │   │   └── Profile.vue                 # 个人资料
│   │   ├── 📂 components/                  # 公共组件
│   │   │   └── Layout.vue                  # 布局组件
│   │   ├── 📂 api/                         # API 接口封装
│   │   ├── 📂 stores/                      # Pinia 状态管理
│   │   ├── 📂 router/                      # 路由配置
│   │   ├── 📂 utils/                       # 工具函数
│   │   ├── App.vue                         # 根组件
│   │   └── main.js                         # 应用入口
│   ├── 📄 package.json                     # npm 依赖配置
│   ├── 📄 vite.config.js                   # Vite 配置
│   └── 📄 index.html                       # HTML 入口
│
├── 📂 campus-secondhand/                   # 旧后端项目 (保留)
├── 📂 campus-secondhand-frontend/          # 旧前端项目 (保留)
│
├── 📄 README.md                            # 项目说明文档
└── 📄 .gitignore                           # Git 忽略配置
```

***

## 🚀 快速开始

### 环境要求

- ✅ **JDK 17+** (必须，Spring Boot 3.x 要求)
- ✅ **MySQL 9.1.0+** (或 8.0+)
- ✅ **Node.js 18+** (推荐 18.x 或 20.x)
- ✅ **Maven 3.6+**

### 1️⃣ 数据库配置

#### 创建数据库

```bash
# 使用 MySQL 命令行
mysql -u root -p

# 创建数据库
CREATE DATABASE IF NOT EXISTS campus_secondhand 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

# 使用数据库
USE campus_secondhand;
```

#### 导入初始化数据

```bash
# 方法 1: 命令行导入
mysql -u root -p campus_secondhand < campus-secondhand-platform/src/main/resources/init-db.sql

# 方法 2: 在 MySQL 客户端执行
source campus-secondhand-platform/src/main/resources/init-db.sql
```

#### 配置数据库连接

编辑 `campus-secondhand-platform/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_secondhand?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password  # 修改为你的数据库密码
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### 2️⃣ 启动后端

```bash
# 进入后端项目目录
cd campus-secondhand-platform

# 使用 Maven 启动
mvn spring-boot:run

# 或者先编译再运行
mvn clean install
mvn spring-boot:run
```

✅ 后端服务将在 **http://localhost:8080/api** 启动

### 3️⃣ 启动前端

```bash
# 进入前端项目目录
cd campus-secondhand-platform-frontend

# 安装依赖 (首次运行需要)
npm install
# 或使用国内镜像加速
npm config set registry https://registry.npmmirror.com
npm install

# 启动开发服务器
npm run dev

# 或指定 npm 路径 (Windows PowerShell 安全策略问题)
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
npm run dev
```

✅ 前端服务将在 **http://localhost:3000** 启动

### 4️⃣ 访问应用

打开浏览器访问：**http://localhost:3000**

***

## 👤 测试账号

### 管理员账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |

### 普通用户账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| zhangsan | 123456 | 普通用户 |
| lisi | 123456 | 普通用户 |
| wangwu | 123456 | 普通用户 |
| zhaoliu | 123456 | 普通用户 |
| sunqi | 123456 | 普通用户 |

> 💡 **注意**：首次使用时建议修改默认密码，密码要求：
> - 长度 8-20 位
> - 包含大小写字母、数字、特殊符号中的至少三种
> - 不使用常见弱密码

***

## 🎨 UI 设计

### 设计理念

- **风格定位**：现代清新校园风
- **主色调**：翡翠绿 (#00b894) + 天空蓝 (#0984e3)
- **设计特点**：年轻、活力、清新、专业

### 设计系统

#### 色彩系统

```css
:root {
  --primary-color: #00b894;      /* 翡翠绿主色 */
  --primary-light: #55efc4;      /* 浅色变体 */
  --primary-dark: #00a383;       /* 深色变体 */
  --secondary-color: #0984e3;    /* 天空蓝辅助色 */
  --accent-color: #fd79a8;       /* 粉色强调色 */
  --text-primary: #2d3436;       /* 主文字色 */
  --text-secondary: #636e72;     /* 次要文字色 */
  --text-muted: #b2bec3;         /* 弱化文字色 */
}
```

#### 阴影系统

- `shadow-sm`: 轻微悬浮 (0 2px 4px)
- `shadow-md`: 卡片阴影 (0 4px 12px)
- `shadow-lg`: 强调阴影 (0 8px 24px)
- `shadow-xl`: 强烈阴影 (0 12px 40px)

#### 圆角系统

- `radius-sm`: 8px (小按钮、标签)
- `radius-md`: 12px (卡片、输入框)
- `radius-lg`: 16px (大卡片)
- `radius-xl`: 24px (超大卡片、模态框)

### 页面设计亮点

#### 1. 布局组件 (Layout.vue)

- **玻璃态导航栏**：半透明背景 + 毛玻璃效果
- **渐变品牌 Logo**：绿色渐变图标 + 渐变文字
- **胶囊搜索框**：圆角设计，聚焦绿色光晕
- **悬浮导航按钮**：上浮效果 + 阴影增强
- **优雅用户下拉**：圆形头像 + 精致下拉菜单

#### 2. 首页 (Home.vue)

- **渐变页面标题**：绿色到蓝色渐变
- **卡片式分类导航**：带图标，悬浮效果
- **现代化商品卡片**：
  - 大圆角设计 (20px)
  - 图片悬浮放大效果
  - 图片遮罩查看按钮
  - 分类标签（绿色半透明）
  - 渐变标题文字
  - 醒目红色价格
  - 浏览/收藏统计

#### 3. 登录页 (Login.vue)

- **紫色渐变背景**：#667eea → #764ba2
- **浮动装饰圆形**：4 个半透明圆形浮动动画
- **玻璃态卡片**：半透明白色 + 毛玻璃
- **脉冲动画 Logo**：80px 渐变图标
- **图标输入框**：圆角设计，聚焦绿色光晕
- **特性展示区**：3 个半透明卡片

#### 4. 注册页 (Register.vue)

- 与登录页一致的设计风格
- 6 个输入字段（带图标）
- 实时验证反馈
- 密码强度指示器
- 渐变按钮

#### 5. 个人中心 (My.vue)

- **标签页切换**：出售中、已卖出、已下架
- **商品管理表格**：支持编辑、上下架、删除操作
- **删除确认对话框**：简洁明了的警告提示
- **操作按钮优化**：合理布局，颜色区分

### 动画效果

- **fadeIn**：页面淡入
- **slideIn**：卡片滑入
- **cardFadeIn**：商品卡片淡入上浮
- **logoPulse**：Logo 脉冲缩放
- **float**：背景圆形浮动

### 响应式设计

- ✅ 桌面端 (1920px+)
- ✅ 笔记本 (1366px)
- ✅ 平板 (768px - 992px)
- ✅ 手机 (375px - 768px)

***

## 📦 功能模块

### 用户端功能

#### 1. 用户认证 🔐

- [x] 用户注册（支持密码强度验证）
- [x] 用户登录（BCrypt 密码验证）
- [x] JWT Token 认证
- [x] Token 自动刷新
- [x] 退出登录
- [x] 用户信息管理
- [x] 修改密码

#### 2. 商品管理 🛍️

- [x] 发布商品（支持多图片上传）
- [x] 编辑商品
- [x] 商品上架/下架
- [x] 商品删除（逻辑删除 + 审计日志）
- [x] 商品状态管理：
  - 出售中 (status=1)
  - 已卖出 (status=3)
  - 已下架 (status=0, 4)
  - 离线状态
- [x] 商品图片上传

#### 3. 商品浏览 🔍

- [x] 首页商品列表
- [x] 商品分类筛选
- [x] 商品搜索
- [x] 商品详情
- [x] 商品推荐
- [x] 浏览量统计

#### 4. 收藏功能 ⭐

- [x] 收藏商品
- [x] 取消收藏
- [x] 我的收藏列表
- [x] 收藏数量统计

#### 5. 订单管理 📦

- [x] 创建订单
- [x] 确认付款
- [x] 确认发货
- [x] 取消订单
- [x] 订单列表（买入/卖出）
- [x] 订单详情
- [x] 订单状态流转追踪

#### 6. 消息中心 💬

- [x] 接收消息
- [x] 查看消息
- [x] 标记已读
- [x] 未读消息计数
- [x] 消息发送

#### 7. 个人中心 👤

- [x] 个人资料修改
- [x] 修改密码
- [x] 我的发布（分标签页管理）
- [x] 我的交易
- [x] 我的收藏

### 管理端功能

#### 1. 数据统计 📊

- [x] 用户总数统计
- [x] 商品总数统计
- [x] 在售商品数量
- [x] 已售出商品数量
- [x] 交易统计数据

#### 2. 用户管理 👥

- [x] 查看用户列表
- [x] 启用/禁用用户
- [x] 用户详情查看
- [x] 用户角色管理

#### 3. 商品管理 🛍️

- [x] 查看商品列表
- [x] 下架违规商品
- [x] 商品审核
- [x] 商品删除审计

#### 4. 日志管理 📝

- [x] 登录日志查询
- [x] 商品删除日志查询
- [x] 操作审计追踪
- [x] 异常操作监控

***

## 🔐 安全特性

### 1. 密码安全

#### BCrypt 加密存储

- 使用 BCrypt 强哈希算法
- 自动添加盐值，防止彩虹表攻击
- 支持密码强度验证
- 密码要求：8-20 位，包含大小写字母、数字、特殊符号中的至少三种

#### 密码迁移

- 支持从明文密码迁移到加密密码
- 提供 SQL 迁移脚本
- 迁移过程保留审计日志

### 2. JWT 认证

#### Token 机制

- 使用 JWT 0.12.3 最新版本
- Access Token + Refresh Token 双 Token 机制
- Token 过期自动刷新
- 支持指纹验证，防止 Token 盗用

#### 安全配置

- 密钥长度 256 位
- Token 过期时间可配置
- 支持 Token 黑名单
- 请求拦截器统一处理认证

### 3. 登录审计

#### 登录日志

记录以下信息：
- 登录用户名
- 登录时间
- 登录 IP 地址
- 登录状态（成功/失败）
- 失败原因
- User-Agent 信息

#### 异常检测

- 多次登录失败预警
- 异常 IP 检测
- 暴力破解防护

### 4. 删除审计

#### 删除日志

记录以下信息：
- 被删除商品 ID 和标题
- 删除时间
- 删除操作人 ID 和用户名
- 删除原因
- 操作人类型（用户/管理员）

#### 逻辑删除

- 使用 deleted 标记，不物理删除数据
- 保留 deleteTime 字段记录删除时间
- 支持数据恢复（需要管理员权限）
- 删除操作事务管理，确保数据一致性

### 5. CORS 安全配置

- 严格限制允许的源
- 支持凭证传输
- 限制 HTTP 方法
- 预检请求缓存

***

## 🔌 API 文档

### 基础 URL

```
http://localhost:8080/api
```

### 认证接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/auth/register` | POST | 用户注册 | ❌ |
| `/auth/login` | POST | 用户登录 | ❌ |
| `/auth/info` | GET | 获取用户信息 | ✅ |
| `/auth/logout` | POST | 退出登录 | ✅ |

### 商品接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/products` | GET | 获取商品列表 | ❌ |
| `/products/{id}` | GET | 获取商品详情 | ❌ |
| `/products` | POST | 发布商品 | ✅ |
| `/products/{id}` | PUT | 修改商品 | ✅ |
| `/products/{id}` | DELETE | 删除商品 | ✅ |
| `/products/my` | GET | 我的商品 | ✅ |
| `/products/categories` | GET | 商品分类 | ❌ |

**删除商品请求参数：**
```json
DELETE /api/products/123?reason=用户主动删除
```

### 收藏接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/favorites` | GET | 收藏列表 | ✅ |
| `/favorites/{productId}` | POST | 添加收藏 | ✅ |
| `/favorites/{productId}` | DELETE | 取消收藏 | ✅ |

### 订单接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/orders` | POST | 创建订单 | ✅ |
| `/orders/buy` | GET | 买入订单列表 | ✅ |
| `/orders/sell` | GET | 卖出订单列表 | ✅ |
| `/orders/{id}/pay` | PUT | 确认付款 | ✅ |
| `/orders/{id}/finish` | PUT | 确认发货 | ✅ |
| `/orders/{id}/cancel` | PUT | 取消订单 | ✅ |

### 消息接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/messages` | POST | 发送消息 | ✅ |
| `/messages/received` | GET | 接收的消息 | ✅ |
| `/messages/read/{messageId}` | PUT | 标记已读 | ✅ |
| `/messages/unread/count` | GET | 未读消息数量 | ✅ |

### 用户接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/user/info` | GET | 获取个人信息 | ✅ |
| `/user/info` | PUT | 修改个人信息 | ✅ |
| `/user/password` | PUT | 修改密码 | ✅ |

### 管理接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/admin/users` | GET | 用户列表 | ✅ (管理员) |
| `/admin/users/{id}/status` | PUT | 修改用户状态 | ✅ (管理员) |
| `/admin/products` | GET | 商品列表 | ✅ (管理员) |
| `/admin/products/{id}` | DELETE | 下架商品 | ✅ (管理员) |
| `/admin/stats` | GET | 统计数据 | ✅ (管理员) |

### 日志接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/admin/logs/login` | GET | 登录日志列表 | ✅ (管理员) |
| `/admin/logs/delete` | GET | 删除日志列表 | ✅ (管理员) |

***

## 🗄️ 数据库表结构

### users - 用户表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(255) | 密码（BCrypt 加密） |
| nickname | VARCHAR(50) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(50) | 邮箱 |
| avatar | VARCHAR(255) | 头像 |
| gender | TINYINT | 性别 |
| school | VARCHAR(100) | 学校 |
| college | VARCHAR(100) | 学院 |
| grade | VARCHAR(20) | 年级 |
| role | TINYINT | 角色 (0:普通用户 1:管理员) |
| status | TINYINT | 状态 (0:禁用 1:正常) |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### products - 商品表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| description | TEXT | 描述 |
| price | DECIMAL(10,2) | 价格 |
| category | VARCHAR(50) | 分类 |
| condition | VARCHAR(50) | 新旧程度 |
| status | INT | 状态 (0:下架 1:在售 3:已售出 4:已下架) |
| deleted | BOOLEAN | 是否删除 (true:已删除) |
| delete_time | DATETIME | 删除时间 |
| seller_id | BIGINT | 卖家 ID |
| seller_name | VARCHAR(50) | 卖家名称 |
| images | VARCHAR(255) | 图片 |
| contact | VARCHAR(50) | 联系方式 |
| location | VARCHAR(100) | 交易地点 |
| view_count | INT | 浏览量 |
| like_count | INT | 点赞数 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### orders - 订单表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| order_no | VARCHAR(100) | 订单号 |
| product_id | BIGINT | 商品 ID |
| product_title | VARCHAR(200) | 商品标题 |
| buyer_id | BIGINT | 买家 ID |
| buyer_name | VARCHAR(50) | 买家名称 |
| seller_id | BIGINT | 卖家 ID |
| seller_name | VARCHAR(50) | 卖家名称 |
| price | DECIMAL(10,2) | 价格 |
| status | INT | 状态 (0:待付款 1:待发货 2:已完成 -1:已取消) |
| remark | VARCHAR(255) | 备注 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### messages - 消息表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| sender_id | BIGINT | 发送者 ID |
| sender_name | VARCHAR(50) | 发送者名称 |
| receiver_id | BIGINT | 接收者 ID |
| receiver_name | VARCHAR(50) | 接收者名称 |
| content | TEXT | 内容 |
| status | INT | 状态 (0:未读 1:已读) |
| product_id | BIGINT | 商品 ID |
| product_title | VARCHAR(200) | 商品标题 |
| create_time | DATETIME | 创建时间 |

### favorites - 收藏表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户 ID |
| product_id | BIGINT | 商品 ID |
| create_time | DATETIME | 创建时间 |

### login_logs - 登录日志表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 登录用户名 |
| login_time | DATETIME | 登录时间 |
| ip_address | VARCHAR(50) | 登录 IP |
| status | INT | 状态 (0:失败 1:成功) |
| error_message | VARCHAR(255) | 失败原因 |
| user_agent | VARCHAR(500) | User-Agent |

### product_delete_logs - 商品删除日志表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| product_id | BIGINT | 商品 ID |
| product_title | VARCHAR(200) | 商品标题 |
| delete_time | DATETIME | 删除时间 |
| operator_id | BIGINT | 操作人 ID |
| operator_name | VARCHAR(50) | 操作人名称 |
| operator_type | VARCHAR(20) | 操作人类型 (USER/ADMIN) |
| reason | VARCHAR(255) | 删除原因 |

***

## ❓ 常见问题

### Q1: 数据库连接失败？

**解决方案：**

1. 检查 MySQL 服务是否启动
2. 确认数据库连接配置正确
3. 检查数据库密码是否正确
4. 确认端口 3306 未被占用

```bash
# 检查 MySQL 服务状态（Windows）
net start | findstr MySQL

# 启动 MySQL 服务
net start MySQL
```

### Q2: 后端启动失败？

**解决方案：**

1. 确认使用 Java 17
2. 检查数据库连接
3. 查看端口 8080 是否被占用
4. 清理 Maven 缓存

```bash
# 检查 Java 版本
java -version

# 清理 Maven 缓存
mvn clean

# 检查端口占用（Windows）
netstat -ano | findstr :8080
```

### Q3: 前端无法启动？

**解决方案：**

1. 检查 Node.js 版本
2. 删除 node_modules 重新安装
3. 使用国内镜像加速

```bash
# 检查 Node.js 版本
node -v

# 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 重新安装依赖
rm -rf node_modules package-lock.json
npm install
```

### Q4: 跨域问题？

**解决方案：**
项目已配置 CORS，如仍有问题：

1. 检查后端 CORS 配置
2. 清除浏览器缓存
3. 检查浏览器控制台错误信息

### Q5: 前端无法访问后端 API？

**解决方案：**

1. 确认后端服务已启动
2. 检查 API 地址是否正确
3. 查看浏览器控制台网络请求
4. 检查 Token 是否有效

### Q6: 登录时提示密码错误？

**解决方案：**

1. 确认密码是否正确输入
2. 检查是否已执行密码迁移脚本
3. 查看后端日志确认错误信息
4. 尝试使用测试账号登录

### Q7: 删除商品功能不工作？

**解决方案：**

1. 确认用户是否有删除权限（必须为商品所有者）
2. 检查商品是否已被删除
3. 查看删除日志确认操作记录
4. 检查后端日志排查错误

### Q8: 密码强度验证不通过？

**密码要求：**

- 长度 8-20 位
- 包含大写字母、小写字母、数字、特殊符号中的至少三种
- 不能使用常见弱密码（如 123456、password 等）

**示例强密码：**
- `Abc123456!`
- `Test@2024`
- `Campus#Trade99`

***

## 📝 开发说明

### 后端开发

#### 项目分层

- **Entity 层**：`entity` 包，JPA 实体类
- **Repository 层**：`repository` 包，数据访问接口
- **Service 层**：`service` 包，业务逻辑
- **Controller 层**：`controller` 包，REST API 接口
- **Config 层**：`config` 包，配置类

#### 开发规范

1. 实体类使用 Jakarta EE 注解
2. Repository 继承 JpaRepository
3. Controller 使用 RESTful 风格
4. 统一返回 Result 对象
5. 使用 JWT 进行认证
6. 敏感操作记录审计日志

### 前端开发

#### 目录结构

- **views/**：页面级组件
- **components/**：公共组件
- **api/**：API 接口封装
- **stores/**：Pinia 状态管理
- **router/**：路由配置
- **utils/**：工具函数

#### 开发规范

1. 使用 Vue 3 Composition API
2. 使用 Pinia 进行状态管理
3. 使用 Element Plus 组件库
4. 统一使用 request.js 封装的 Axios
5. 遵循 Vue 3 最佳实践
6. 关键操作添加确认提示

***

## 🧪 测试

### 单元测试

```bash
# 运行所有测试
cd campus-secondhand-platform
mvn test

# 运行特定测试类
mvn test -Dtest=ProductDeleteControllerTest
```

### 集成测试

测试报告详见：
- [删除功能测试报告](campus-secondhand-platform/DELETE_FEATURE_TEST_REPORT.md)
- [测试指南](campus-secondhand-platform/TESTING_GUIDE.md)

***

## 🔮 后续优化方向

### 功能扩展

- [ ] 图片上传功能（七牛云/阿里云 OSS）
- [ ] 商品图片展示优化
- [ ] 即时通讯功能（WebSocket）
- [ ] 支付接口集成（支付宝/微信）
- [ ] 商品推荐算法
- [ ] 搜索功能增强（Elasticsearch）
- [ ] 用户信用评价体系
- [ ] 商品举报功能
- [ ] 站内通知系统

### 性能优化

- [ ] 图片懒加载
- [ ] 虚拟滚动
- [ ] 服务端渲染（SSR）
- [ ] CDN 部署
- [ ] Service Worker 缓存
- [ ] 代码分割
- [ ] Redis 缓存

### 用户体验

- [ ] 暗黑模式
- [ ] 主题色自定义
- [ ] 骨架屏加载
- [ ] 空状态优化
- [ ] 错误页面设计
- [ ] 引导提示优化
- [ ] 快捷键支持

### 技术升级

- [ ] TypeScript 支持
- [ ] 单元测试（Jest/Vitest）
- [ ] E2E 测试（Cypress/Playwright）
- [ ] Docker 容器化
- [ ] CI/CD 流程
- [ ] 自动化部署

***

## 📚 参考文档

### 官方文档

- [Spring Boot 3 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vue 3 文档](https://vuejs.org/)
- [Vite 5 文档](https://vitejs.dev/)
- [Element Plus 文档](https://element-plus.org/)
- [JWT 文档](https://github.com/jwtk/jjwt)
- [BCrypt 文档](https://github.com/jeremyh/jBCrypt)

### 迁移指南

- [Spring Boot 3 迁移指南](https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0)
- [Jakarta EE 9+ 迁移](https://jakarta.ee/blogs/jakarta-ee-9-migration/)
- [Vue 3.4 发布说明](https://blog.vuejs.org/posts/vue-3-4)
- [Vite 5 发布说明](https://vitejs.dev/blog/announcing-vite5)

***

## 📄 许可证

本项目仅供学习参考使用。

***

## 👨‍💻 项目信息

- **技术栈**：Vue 3.4.21 + Vite 5.2.8 + Spring Boot 3.2.0 + Java 17
- **设计风格**：现代清新校园风
- **主色调**：翡翠绿 (#00b894) + 天空蓝 (#0984e3)
- **开发时间**：2024
- **适用场景**：校园二手交易、毕业设计、学习项目
- **安全特性**：JWT 认证、BCrypt 加密、登录审计、删除审计

***

<div align="center">

**🎉 祝您使用愉快！**

[⬆ 返回顶部](#-校园二手交易平台)

</div>
