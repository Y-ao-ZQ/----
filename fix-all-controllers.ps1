# 修复所有 Controller 文件的编码问题 - 将中文替换为英文
$controllerDir = "d:\校园二手\campus-secondhand-platform\src\main\java\com\campus\secondhand\controller"

# 定义中英文映射
$replacements = @{
    '用户不存在' = 'User not found'
    '密码错误' = 'Wrong password'
    '用户已禁用' = 'User disabled'
    '登录成功' = 'Login successful'
    '退出成功' = 'Logout successful'
    '商品不存在' = 'Product not found'
    '发布成功' = 'Publish successful'
    '修改成功' = 'Update successful'
    '删除成功' = 'Delete successful'
    '状态更新成功' = 'Status updated'
    '无权限修改此商品' = 'No permission to modify'
    '无权限删除此商品' = 'No permission to delete'
    '无权限操作此商品' = 'No permission'
    '收藏成功' = 'Favorite added'
    '取消收藏成功' = 'Favorite removed'
    '订单不存在' = 'Order not found'
    '订单已取消' = 'Order cancelled'
    '订单已完成' = 'Order completed'
    '订单已付款' = 'Order paid'
    '订单已发货' = 'Order shipped'
    '创建订单成功' = 'Order created'
    '无权限操作' = 'No permission'
    '无权限查看' = 'No permission'
    '消息发送成功' = 'Message sent'
    '已标记为已读' = 'Marked as read'
    '发送成功' = 'Sent successfully'
    '更新成功' = 'Updated successfully'
    '个人信息更新成功' = 'Profile updated'
    '密码修改成功' = 'Password changed'
    '原密码错误' = 'Wrong old password'
    '用户列表' = 'User list'
    '商品列表' = 'Product list'
    '统计数据' = 'Statistics'
    '商品不存' = 'Product not found'
    '用户不存' = 'User not found'
    '成？' = 'successful'
    '成功' = 'successful'
    '发送成' = 'Sent'
}

# 处理所有 Controller 文件
$files = @('AuthController.java', 'UserController.java', 'ProductController.java', 
           'MessageController.java', 'OrderController.java', 'AdminController.java', 
           'FavoriteController.java')

foreach ($file in $files) {
    $filePath = Join-Path $controllerDir $file
    if (Test-Path $filePath) {
        # 读取文件内容
        $content = Get-Content $filePath -Raw -Encoding UTF8
        
        # 移除 BOM
        if ($content.StartsWith([char]0xFEFF)) {
            $content = $content.Substring(1)
        }
        
        # 替换所有中文
        foreach ($key in $replacements.Keys) {
            $content = $content -replace [regex]::Escape($key), $replacements[$key]
        }
        
        # 保存文件（无 BOM）
        [System.IO.File]::WriteAllText($filePath, $content, [System.Text.UTF8Encoding]::new($false))
        Write-Host "✓ Fixed: $file"
    }
}

Write-Host "`n✅ All controllers fixed!"
