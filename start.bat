@echo off
echo ========================================
echo   校园二手交易平台 - 启动脚本
echo ========================================
echo.

echo [1/3] 正在启动后端服务...
cd campus-secondhand
start "Campus Secondhand Backend" cmd /k "mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [2/3] 正在启动前端服务...
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
