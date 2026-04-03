# 校园二手交易平台 - 部署指南

## 重要提示

**在启动项目之前，请先安装并配置 MySQL 数据库！**

## 环境要求

### 必需软件
1. **JDK 1.8+**
   - 下载地址：https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
   - 验证安装：`java -version`

2. **Maven 3.6+**
   - 下载地址：https://maven.apache.org/download.cgi
   - 验证安装：`mvn -version`

3. **MySQL 8.0+**
   - 下载地址：https://dev.mysql.com/downloads/mysql/
   - 验证安装：`mysql --version`

4. **Node.js 16+**
   - 下载地址：https://nodejs.org/
   - 验证安装：`node -v` 和 `npm -v`

## 第一步：安装和配置 MySQL

### Windows 安装 MySQL

1. 下载 MySQL Installer for Windows
2. 运行安装程序，选择"Developer Default"或"Server only"
3. 设置 root 密码（建议设置为：root123456）
4. 记住你的 MySQL 端口号（默认 3306）

### 创建数据库

方法一：使用 MySQL 命令行工具
```bash
mysql -u root -p
```
输入密码后，执行：
```sql
CREATE DATABASE IF NOT EXISTS campus_secondhand DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

方法二：使用 MySQL Workbench 或其他图形化工具
1. 连接到 MySQL 服务器
2. 右键点击"Schemas"
3. 选择"Create Schema"
4. 输入数据库名：campus_secondhand
5. 点击"Apply"

### 导入测试数据

1. 打开 MySQL 命令行或 Workbench
2. 执行以下命令：
```sql
USE campus_secondhand;
```

3. 运行项目中的 SQL 脚本：
```bash
mysql -u root -p campus_secondhand < campus-secondhand/src/main/resources/init-db.sql
```

或在 MySQL Workbench 中打开并执行 `init-db.sql` 文件。

## 第二步：配置后端

### 修改数据库连接配置

编辑文件：`campus-secondhand/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_secondhand?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root          # 修改为你的 MySQL 用户名
    password: your_password # 修改为你的 MySQL 密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**重要：** 确保将 `password` 修改为你实际的 MySQL 密码！

## 第三步：启动后端服务

### 方法一：使用 Maven 命令

```bash
cd campus-secondhand
mvn spring-boot:run
```

### 方法二：使用 IDE（推荐）

1. 使用 IntelliJ IDEA 或 Eclipse 打开 `campus-secondhand` 项目
2. 找到 `CampusSecondhandApplication.java` 文件
3. 右键点击，选择"Run"或"Debug"

### 验证后端启动成功

当看到以下日志时，表示后端启动成功：
```
Started CampusSecondhandApplication in X.XXX seconds
```

访问：http://localhost:8080/api/products 应该能返回商品列表

## 第四步：启动前端服务

### 安装依赖（如果还未安装）

```bash
cd campus-secondhand-frontend
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 验证前端启动成功

当看到以下日志时，表示前端启动成功：
```
  VITE v4.x.x  ready in xxx ms

  ➜  Local:   http://localhost:3000/
  ➜  Network: use --host to expose
```

打开浏览器访问：http://localhost:3000

## 第五步：测试系统

### 测试账号

#### 管理员账号
- 用户名：admin
- 密码：admin123
- 功能：可以访问后台管理页面，管理用户和商品

#### 学生账号（5 个）
1. 用户名：zhangsan / 密码：123456
2. 用户名：lisi / 密码：123456
3. 用户名：wangwu / 密码：123456
4. 用户名：zhaoliu / 密码：123456
5. 用户名：sunqi / 密码：123456

### 功能测试清单

#### 1. 用户注册和登录
- [ ] 访问 http://localhost:3000/register 注册新账号
- [ ] 使用测试账号登录
- [ ] 验证登录成功后跳转到首页

#### 2. 商品浏览
- [ ] 查看首页商品列表
- [ ] 点击商品分类筛选商品
- [ ] 使用搜索功能搜索商品
- [ ] 点击商品查看详情

#### 3. 商品发布
- [ ] 点击"发布"按钮
- [ ] 填写商品信息（标题、价格、分类、描述等）
- [ ] 提交发布
- [ ] 在"我的"页面查看已发布的商品

#### 4. 收藏功能
- [ ] 在商品详情页点击"收藏"
- [ ] 验证收藏成功
- [ ] 在"我的收藏"页面查看收藏的商品
- [ ] 取消收藏

#### 5. 订单功能
- [ ] 在商品详情页点击"我想要"
- [ ] 创建订单
- [ ] 在"订单"页面查看订单
- [ ] 买家确认付款
- [ ] 卖家确认发货
- [ ] 验证订单状态变化

#### 6. 消息功能
- [ ] 在商品详情页点击"联系我"
- [ ] 发送消息
- [ ] 在"消息"页面查看收到的消息
- [ ] 标记消息为已读

#### 7. 个人中心
- [ ] 修改个人资料
- [ ] 修改密码
- [ ] 验证修改成功

#### 8. 后台管理（仅管理员）
- [ ] 使用 admin 账号登录
- [ ] 访问后台管理页面
- [ ] 查看数据统计
- [ ] 管理用户（启用/禁用）
- [ ] 管理商品（下架商品）

## 常见问题

### 1. 数据库连接失败

**错误信息：** `Communications link failure`

**解决方案：**
- 检查 MySQL 服务是否启动
- 检查数据库连接配置（用户名、密码、端口）
- 确认数据库 `campus_secondhand` 已创建

### 2. 前端无法访问后端

**错误信息：** `Network Error` 或 `404`

**解决方案：**
- 确认后端服务已启动（访问 http://localhost:8080/api/products 测试）
- 检查前端代理配置（vite.config.js）
- 检查浏览器控制台是否有 CORS 错误

### 3. 前端页面空白

**解决方案：**
- 打开浏览器控制台查看错误信息
- 确认前端依赖已正确安装（`npm install`）
- 清除浏览器缓存

### 4. 登录失败

**错误信息：** "用户不存在"或"密码错误"

**解决方案：**
- 确认数据库已正确导入测试数据
- 检查 users 表中是否有测试账号数据
- 确认密码是否正确（区分大小写）

### 5. Maven 构建失败

**错误信息：** 编译错误或依赖下载失败

**解决方案：**
- 检查 JDK 版本是否为 1.8+
- 检查 Maven 配置
- 尝试清理后重新构建：`mvn clean install`

### 6. npm install 失败

**解决方案：**
- 检查 Node.js 版本是否为 16+
- 删除 node_modules 文件夹和 package-lock.json
- 重新运行：`npm install`
- 如仍有问题，尝试使用淘宝镜像：
  ```bash
  npm config set registry https://registry.npmmirror.com
  npm install
  ```

## 一键启动脚本（可选）

### Windows 批处理脚本

创建 `start.bat` 文件（项目根目录）：

```batch
@echo off
echo ========================================
echo   校园二手交易平台 - 启动脚本
echo ========================================
echo.

echo [1/2] 正在启动后端服务...
cd campus-secondhand
start "Campus Secondhand Backend" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak >nul

echo [2/2] 正在启动前端服务...
cd ..\campus-secondhand-frontend
start "Campus Secondhand Frontend" cmd /k "npm run dev"

echo.
echo ========================================
echo   服务启动中...
echo ========================================
echo   后端地址：http://localhost:8080/api
echo   前端地址：http://localhost:3000
echo.
echo   测试账号:
echo   - 管理员：admin / admin123
echo   - 学生 1: zhangsan / 123456
echo   - 学生 2: lisi / 123456
echo ========================================
echo.
pause
```

**使用方法：** 双击运行 `start.bat`

## 生产环境部署

### 后端打包

```bash
cd campus-secondhand
mvn clean package -DskipTests
```

生成的 jar 包位于：`target/campus-secondhand-1.0.0.jar`

### 前端打包

```bash
cd campus-secondhand-frontend
npm run build
```

生成的文件位于：`dist/` 目录

### 部署步骤

1. 将后端 jar 包部署到服务器
2. 配置生产数据库连接
3. 将前端 dist 目录部署到 Nginx 或其他 Web 服务器
4. 配置 Nginx 反向代理到后端服务

## 技术栈说明

### 后端
- **Spring Boot 2.7.18** - 快速开发框架
- **Spring Data JPA** - 数据访问层
- **MySQL 8.0** - 关系型数据库
- **JWT** - 用户认证
- **Lombok** - 代码简化

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Element Plus** - UI 组件库
- **Axios** - HTTP 客户端
- **Vite** - 构建工具

## 项目结构

```
校园二手/
├── campus-secondhand/          # 后端项目
│   ├── src/main/java/
│   │   └── com/campus/secondhand/
│   │       ├── entity/         # 实体类
│   │       ├── repository/     # 数据访问层
│   │       ├── controller/     # 控制器
│   │       ├── config/         # 配置类
│   │       ├── interceptor/    # 拦截器
│   │       ├── util/           # 工具类
│   │       └── dto/            # 数据传输对象
│   └── src/main/resources/
│       ├── application.yml     # 配置文件
│       └── init-db.sql         # 数据库初始化脚本
├── campus-secondhand-frontend/ # 前端项目
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── api/                # API 接口
│   │   ├── stores/             # 状态管理
│   │   ├── router/             # 路由
│   │   └── utils/              # 工具类
│   └── package.json
├── README.md                   # 项目说明
├── DEPLOYMENT.md              # 部署指南（本文件）
└── start.bat                  # 启动脚本
```

## 下一步开发建议

1. **图片上传功能** - 添加商品图片上传
2. **即时通讯** - 实现实时聊天功能
3. **支付集成** - 接入支付宝/微信支付
4. **商品推荐** - 基于用户行为的推荐算法
5. **移动端优化** - 响应式设计或开发小程序
6. **用户评价** - 建立信用评价体系
7. **搜索优化** - 引入 Elasticsearch
8. **数据统计** - 完善数据分析功能

## 联系和支持

如有问题，请查看：
- 项目 README.md 文件
- 后端日志（控制台输出）
- 前端浏览器控制台

---

**祝部署成功！** 🎉
