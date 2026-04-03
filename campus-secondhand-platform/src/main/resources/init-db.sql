-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_secondhand DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_secondhand;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    gender VARCHAR(50),
    school VARCHAR(100),
    college VARCHAR(100),
    grade VARCHAR(20),
    role INT NOT NULL DEFAULT 0,
    status INT NOT NULL DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    `condition` VARCHAR(50),
    status INT NOT NULL DEFAULT 1,
    seller_id BIGINT NOT NULL,
    seller_name VARCHAR(50),
    images VARCHAR(255),
    contact VARCHAR(50),
    location VARCHAR(100),
    view_count INT NOT NULL DEFAULT 0,
    like_count INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL,
    product_title VARCHAR(200) NOT NULL,
    buyer_id BIGINT NOT NULL,
    buyer_name VARCHAR(50),
    seller_id BIGINT NOT NULL,
    seller_name VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    status INT NOT NULL DEFAULT 0,
    remark VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pay_time TIMESTAMP NULL,
    finish_time TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 消息表
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    sender_name VARCHAR(50),
    receiver_id BIGINT NOT NULL,
    receiver_name VARCHAR(50),
    content VARCHAR(1000) NOT NULL,
    status INT NOT NULL DEFAULT 0,
    product_id BIGINT,
    product_title VARCHAR(200),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试数据 - 管理员账号
INSERT INTO users (username, password, nickname, phone, email, role, status) VALUES
('admin', 'admin123', '管理员', '13800000000', 'admin@campus.com', 1, 1);

-- 插入测试数据 - 学生账号
INSERT INTO users (username, password, nickname, phone, email, gender, school, college, grade, role, status) VALUES
('zhangsan', '123456', '张三', '13800138001', 'zhangsan@stu.edu.cn', '男', 'XX 大学', '计算机学院', '2021', 0, 1),
('lisi', '123456', '李四', '13800138002', 'lisi@stu.edu.cn', '女', 'XX 大学', '电子信息学院', '2022', 0, 1),
('wangwu', '123456', '王五', '13800138003', 'wangwu@stu.edu.cn', '男', 'XX 大学', '经济管理学院', '2020', 0, 1),
('zhaoliu', '123456', '赵六', '13800138004', 'zhaoliu@stu.edu.cn', '女', 'XX 大学', '文学院', '2023', 0, 1),
('sunqi', '123456', '孙七', '13800138005', 'sunqi@stu.edu.cn', '男', 'XX 大学', '机械学院', '2021', 0, 1);

-- 插入测试数据 - 商品
INSERT INTO products (title, description, price, category, condition, status, seller_id, seller_name, contact, location) VALUES
('iPhone 13 二手手机', '95 新，无划痕，电池健康度 95%，配件齐全', 3500.00, '数码产品', '95 新', 1, 2, '张三', '13800138001', 'XX 大学 3 号楼'),
('MacBook Pro 2020', 'M1 芯片，8G+256G，轻微使用痕迹', 6500.00, '数码产品', '9 成新', 1, 2, '张三', '13800138001', 'XX 大学 3 号楼'),
('高等数学教材', '同济版第七版，有少量笔记', 15.00, '图书教材', '8 成新', 1, 3, '李四', '13800138002', 'XX 大学 5 号楼'),
('英语四六级资料', '包含真题和模拟卷，几乎全新', 25.00, '图书教材', '95 新', 1, 3, '李四', '13800138002', 'XX 大学 5 号楼'),
('山地自行车', '捷安特，3 年车龄，功能正常', 300.00, '运动户外', '7 成新', 1, 4, '王五', '13800138003', 'XX 大学 7 号楼'),
('篮球鞋', '耐克，42 码，穿过几次', 200.00, '服饰鞋包', '9 成新', 1, 4, '王五', '13800138003', 'XX 大学 7 号楼'),
('台灯', 'LED 护眼台灯，可充电', 50.00, '生活用品', '95 新', 1, 5, '赵六', '13800138004', 'XX 大学 9 号楼'),
('收纳箱', '大号，塑料材质，几乎没用过', 30.00, '生活用品', '9 成新', 1, 5, '赵六', '13800138004', 'XX 大学 9 号楼'),
('吉他', '雅马哈 F310，初学者入门款', 600.00, '其他', '8 成新', 1, 6, '孙七', '13800138005', 'XX 大学 11 号楼'),
('考研数学复习资料', '张宇 18 讲，有笔记', 40.00, '图书教材', '8 成新', 1, 6, '孙七', '13800138005', 'XX 大学 11 号楼'),
('iPad Air 4', '64G WiFi 版，深空灰，带保护套', 3200.00, '数码产品', '95 新', 3, 2, '张三', '13800138001', 'XX 大学 3 号楼'),
('管理学原理', '高教版，有少量划线', 20.00, '图书教材', '8 成新', 3, 3, '李四', '13800138002', 'XX 大学 5 号楼');
