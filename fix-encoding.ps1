# 修复所有 Controller 文件的中文编码问题
$controllers = @('AuthController.java', 'UserController.java', 'ProductController.java', 'MessageController.java', 'OrderController.java', 'AdminController.java', 'FavoriteController.java')

foreach ($controller in $controllers) {
    if (Test-Path $controller) {
        $content = Get-Content $controller -Raw -Encoding UTF8
        # 替换常见中文消息
        $content = $content -replace '用户不存在', 'User not found'
        $content = $content -replace '密码错误', 'Wrong password'
        $content = $content -replace '用户已禁用', 'User disabled'
        $content = $content -replace '登录成功', 'Login success'
        $content = $content -replace '退出成功', 'Logout success'
        $content = $content -replace '商品不存在', 'Product not found'
        $content = $content -replace '发布成功', 'Publish success'
        $content = $content -replace '修改成功', 'Update success'
        $content = $content -replace '删除成功', 'Delete success'
        $content = $content -replace '状态更新成功', 'Status updated'
        $content = $content -replace '无权限修改此商品', 'No permission'
        $content = $content -replace '无权限删除此商品', 'No permission'
        $content = $content -replace '无权限操作此商品', 'No permission'
        $content = $content -replace '收藏成功', 'Favorite added'
        $content = $content -replace '取消收藏成功', 'Favorite removed'
        $content = $content -replace '订单不存在', 'Order not found'
        $content = $content -replace '订单已取消', 'Order cancelled'
        $content = $content -replace '订单已完成', 'Order completed'
        $content = $content -replace '订单已付款', 'Order paid'
        $content = $content -replace '订单已发货', 'Order shipped'
        $content = $content -replace '创建订单成功', 'Order created'
        $content = $content -replace '无权限操作', 'No permission'
        $content = $content -replace '无权限查看', 'No permission'
        $content = $content -replace '消息发送成功', 'Message sent'
        $content = $content -replace '已标记为已读', 'Marked as read'
        $content = $content -replace '发送成功', 'Sent success'
        $content = $content -replace '更新成功', 'Updated success'
        $content = $content -replace '个人信息更新成功', 'Profile updated'
        $content = $content -replace '密码修改成功', 'Password changed'
        $content = $content -replace '原密码错误', 'Wrong old password'
        $content = $content -replace '用户列表', 'User list'
        $content = $content -replace '商品列表', 'Product list'
        $content = $content -replace '统计数据', 'Statistics'
        Set-Content $controller $content -Encoding UTF8 -NoNewline
        Write-Host "Fixed $controller"
    }
}

Write-Host "All controllers fixed!"
