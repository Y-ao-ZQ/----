<template>
  <div class="login-page">
    <div class="background">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
      <div class="bg-shape shape-4"></div>
    </div>
    
    <div class="login-container">
      <div class="login-card">
        <div class="card-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <el-icon><ShoppingCart /></el-icon>
            </div>
          </div>
          <h2 class="title">欢迎回来</h2>
          <p class="subtitle">登录校园二手交易平台</p>
        </div>
        
        <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
          <el-form-item prop="username">
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input 
                v-model="form.username" 
                placeholder="请输入账号" 
                class="custom-input"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <el-input 
                v-model="form.password" 
                type="password" 
                placeholder="请输入密码" 
                show-password
                class="custom-input"
                @keyup.enter="handleLogin"
              />
            </div>
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              :loading="loading" 
              @click="handleLogin" 
              class="login-btn"
            >
              <span v-if="!loading">登录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="card-footer">
          <p class="tips">
            还没有账号？
            <router-link to="/register" class="register-link">立即注册</router-link>
          </p>
        </div>
      </div>
      
      <div class="features">
        <div class="feature-item">
          <div class="feature-icon">
            <el-icon><ShoppingBag /></el-icon>
          </div>
          <h3>海量好物</h3>
          <p>校园闲置物品，应有尽有</p>
        </div>
        <div class="feature-item">
          <div class="feature-icon">
            <el-icon><SafetyCertificate /></el-icon>
          </div>
          <h3>安全可信</h3>
          <p>实名认证，交易更放心</p>
        </div>
        <div class="feature-item">
          <div class="feature-icon">
            <el-icon><Wallet /></el-icon>
          </div>
          <h3>超值低价</h3>
          <p>学生专属，价格优惠</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(form)
        userStore.setToken(res.data.token)
        userStore.setUserInfo(res.data)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (e) {
        console.error(e)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 70px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.shape-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.shape-2 {
  width: 300px;
  height: 300px;
  top: 50%;
  right: -50px;
  animation-delay: 5s;
}

.shape-3 {
  width: 250px;
  height: 250px;
  bottom: 10%;
  left: 10%;
  animation-delay: 10s;
}

.shape-4 {
  width: 350px;
  height: 350px;
  bottom: -100px;
  right: 20%;
  animation-delay: 15s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(30px, 30px) rotate(90deg);
  }
  50% {
    transform: translate(0, 50px) rotate(180deg);
  }
  75% {
    transform: translate(-30px, 30px) rotate(270deg);
  }
}

.login-container {
  display: flex;
  gap: 60px;
  align-items: center;
  max-width: 1100px;
  width: 100%;
  position: relative;
  z-index: 1;
}

.login-card {
  flex: 1;
  max-width: 440px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 50px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideIn 0.6s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.card-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 40px;
  box-shadow: 0 10px 30px rgba(0, 184, 148, 0.3);
  animation: logoPulse 2s infinite ease-in-out;
}

@keyframes logoPulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.title {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, #00b894 0%, #0984e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 12px;
}

.subtitle {
  font-size: 15px;
  color: var(--text-secondary);
  font-weight: 400;
}

.login-form {
  margin-bottom: 30px;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 16px;
  border: 2px solid rgba(0, 0, 0, 0.05);
  padding: 0 20px;
  transition: all 0.3s ease;
}

.input-wrapper:hover {
  border-color: rgba(0, 184, 148, 0.3);
  box-shadow: 0 4px 16px rgba(0, 184, 148, 0.1);
}

.input-wrapper:focus-within {
  border-color: var(--primary-color);
  box-shadow: 0 6px 24px rgba(0, 184, 148, 0.2);
}

.input-icon {
  font-size: 20px;
  color: var(--text-muted);
  margin-right: 12px;
  transition: color 0.3s ease;
}

.input-wrapper:focus-within .input-icon {
  color: var(--primary-color);
}

.custom-input {
  flex: 1;
}

.custom-input:deep(.el-input__inner) {
  border: none;
  padding: 16px 0;
  font-size: 15px;
  height: auto;
}

.custom-input:deep(.el-input__inner::placeholder) {
  color: var(--text-muted);
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 16px;
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  border: none;
  box-shadow: 0 8px 24px rgba(0, 184, 148, 0.3);
  transition: all 0.3s ease;
  margin-top: 10px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 184, 148, 0.4);
  background: linear-gradient(135deg, #00a383 0%, #00b5a8 100%);
}

.login-btn:active {
  transform: translateY(0);
}

.card-footer {
  text-align: center;
}

.tips {
  font-size: 14px;
  color: var(--text-secondary);
}

.register-link {
  color: var(--primary-color);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

.features {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
  color: white;
  animation: fadeIn 0.8s ease 0.2s both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 12px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-4px);
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  margin-bottom: 8px;
}

.feature-item h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.feature-item p {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

@media (max-width: 992px) {
  .login-container {
    flex-direction: column;
    gap: 40px;
  }
  
  .login-card {
    width: 100%;
    max-width: 440px;
  }
  
  .features {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .feature-item {
    flex: 1;
    min-width: 200px;
    max-width: 300px;
  }
}

@media (max-width: 576px) {
  .login-page {
    padding: 20px 10px;
  }
  
  .login-card {
    padding: 40px 30px;
  }
  
  .title {
    font-size: 26px;
  }
  
  .features {
    flex-direction: column;
  }
  
  .feature-item {
    width: 100%;
  }
}
</style>
