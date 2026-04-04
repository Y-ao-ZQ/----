-- 注意：此脚本用于将现有用户的明文密码更新为加密格式
-- 由于 BCrypt 是随机盐值加密，每次生成的哈希都不同
-- 实际生产中应该在应用层使用 PasswordUtil 进行密码更新

-- 示例：将密码更新为 BCrypt 加密格式（密码：123456）
-- 注意：这只是示例，实际应该通过应用程序进行密码迁移

-- 查看当前用户表
SELECT id, username, password, '当前明文密码' as description FROM user;

-- 警告：不要直接在这里运行 UPDATE，因为 BCrypt 需要程序生成
-- UPDATE user SET password = '$2a$10$...' WHERE id = ...;

-- 正确的做法：
-- 1. 在应用程序中创建密码迁移接口
-- 2. 或者让用户重新设置密码
-- 3. 或者在首次登录时检测明文密码并自动升级
