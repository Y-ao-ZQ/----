<template>
  <div class="register-page">
    <div class="background">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
      <div class="bg-shape shape-4"></div>
    </div>
    
    <div class="register-container">
      <div class="register-card">
        <div class="card-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <el-icon><ShoppingCart /></el-icon>
            </div>
          </div>
          <h2 class="title">创建账号</h2>
          <p class="subtitle">加入我们，开启校园二手之旅</p>
        </div>
        
        <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
          <el-form-item prop="username">
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input 
                v-model="form.username" 
                placeholder="请输入用户名" 
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
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <el-input 
                v-model="form.confirmPassword" 
                type="password" 
                placeholder="请确认密码" 
                show-password
                class="custom-input"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="nickname">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Edit /></el-icon>
              <el-input 
                v-model="form.nickname" 
                placeholder="请输入昵称（可选）" 
                class="custom-input"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="phone">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Phone /></el-icon>
              <el-input 
                v-model="form.phone" 
                placeholder="请输入手机号（可选）" 
                class="custom-input"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="email">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Message /></el-icon>
              <el-input 
                v-model="form.email" 
                placeholder="请输入邮箱（可选）" 
                class="custom-input"
              />
            </div>
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              :loading="loading" 
              @click="handleRegister" 
              class="register-btn"
            >
              <span v-if="!loading">立即注册</span>
              <span v-else>注册中...</span>
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="card-footer">
          <p class="tips">
            已有账号？
            <router-link to="/login" class="login-link">立即登录</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { register } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  phone: '',
  email: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await register(form)
        userStore.setToken(res.data.token)
        userStore.setUserInfo(res.data)
        ElMessage.success('注册成功')
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
.register-page {
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

.register-container {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 500px;
  width: 100%;
  position: relative;
  z-index: 1;
}

.register-card {
  flex: 1;
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
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
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

.register-form {
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

.register-btn {
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

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 184, 148, 0.4);
  background: linear-gradient(135deg, #00a383 0%, #00b5a8 100%);
}

.register-btn:active {
  transform: translateY(0);
}

.card-footer {
  text-align: center;
}

.tips {
  font-size: 14px;
  color: var(--text-secondary);
}

.login-link {
  color: var(--primary-color);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
  transition: all 0.3s ease;
}

.login-link:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

@media (max-width: 576px) {
  .register-page {
    padding: 20px 10px;
  }
  
  .register-card {
    padding: 40px 30px;
  }
  
  .title {
    font-size: 26px;
  }
}
</style>
