<template>
  <div class="layout">
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <div class="logo-icon">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="logo-text">
            <span class="brand">校园二手</span>
            <span class="slogan">发现好物</span>
          </div>
        </div>
        
        <div class="search-bar">
          <el-input
            v-model="keyword"
            placeholder="搜索你感兴趣的宝贝..."
            clearable
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #append>
              <el-button type="primary" @click="handleSearch">
                搜索
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="nav-links">
          <el-button class="nav-btn publish-btn" @click="$router.push('/publish')">
            <el-icon><Plus /></el-icon>
            发布闲置
          </el-button>
          
          <el-button class="nav-btn" @click="$router.push('/messages')" style="position: relative;">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="msg-badge">
              <el-icon><ChatDotRound /></el-icon>
            </el-badge>
            <span>消息</span>
          </el-button>
          
          <el-button class="nav-btn" @click="$router.push('/orders')">
            <el-icon><List /></el-icon>
            <span>订单</span>
          </el-button>
          
          <el-button class="nav-btn" @click="$router.push('/my')">
            <el-icon><User /></el-icon>
            <span>我的</span>
          </el-button>
          
          <template v-if="userStore.token">
            <el-dropdown @command="handleCommand" class="user-dropdown">
              <div class="user-info">
                <el-avatar :size="36" :src="userStore.avatar || ''" class="user-avatar" />
                <span class="nickname">{{ userStore.nickname || userStore.username }}</span>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="dropdown-menu">
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="favorites">
                    <el-icon><Star /></el-icon>我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === 1" command="admin">
                    <el-icon><Setting /></el-icon>后台管理
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          
          <template v-else>
            <el-button class="auth-btn" @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" class="auth-btn register-btn" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    
    <el-main class="main">
      <router-view />
    </el-main>
    
    <el-footer class="footer">
      <div class="footer-content">
        <p class="copyright">校园二手交易平台 © 2024</p>
        <p class="description">让闲置流动起来，让价值延续下去</p>
      </div>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUnreadCount } from '@/api'

const router = useRouter()
const userStore = useUserStore()
const keyword = ref('')
const unreadCount = ref(0)

const handleSearch = () => {
  if (keyword.value.trim()) {
    router.push({ path: '/', query: { keyword: keyword.value } })
  }
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'favorites':
      router.push('/favorites')
      break
    case 'admin':
      router.push('/admin')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}

const loadUnreadCount = async () => {
  if (userStore.token) {
    try {
      const res = await getUnreadCount()
      unreadCount.value = res.data.count
    } catch (e) {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadUnreadCount()
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 1000;
  border-bottom: 1px solid rgba(0, 184, 148, 0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 70px;
  padding: 0 30px;
  gap: 30px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
  flex-shrink: 0;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(0, 184, 148, 0.3);
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.brand {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #00b894 0%, #0984e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.slogan {
  font-size: 11px;
  color: var(--text-muted);
  letter-spacing: 1px;
  margin-top: 2px;
}

.search-bar {
  flex: 1;
  max-width: 500px;
}

.search-input {
  width: 100%;
}

.search-input:deep(.el-input__wrapper) {
  border-radius: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.search-input:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.search-input:deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-color);
  box-shadow: 0 4px 20px rgba(0, 184, 148, 0.2);
}

.search-input:deep(.el-input__inner) {
  height: 44px;
  padding-left: 40px;
  font-size: 14px;
}

.search-input:deep(.el-input-group__append) {
  border-radius: 0 25px 25px 0;
  padding: 0 24px;
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: white;
  font-weight: 500;
}

.search-input:deep(.el-input-group__append:hover) {
  background: var(--primary-dark);
  border-color: var(--primary-dark);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
}

.nav-btn {
  height: 40px;
  padding: 0 16px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.nav-btn:hover {
  background: rgba(0, 184, 148, 0.1);
  color: var(--primary-color);
  border-color: rgba(0, 184, 148, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 184, 148, 0.15);
}

.publish-btn {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  color: white;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 184, 148, 0.3);
}

.publish-btn:hover {
  background: linear-gradient(135deg, #00a383 0%, #00b5a8 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 184, 148, 0.4);
}

.msg-badge:deep(.el-badge__content) {
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(253, 121, 168, 0.4);
}

.user-dropdown {
  margin-left: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(0, 184, 148, 0.1);
  border-color: rgba(0, 184, 148, 0.3);
}

.user-avatar {
  border: 2px solid var(--primary-color);
  box-shadow: 0 2px 8px rgba(0, 184, 148, 0.2);
}

.nickname {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  font-size: 12px;
  color: var(--text-muted);
}

.dropdown-menu {
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border: 1px solid rgba(0, 0, 0, 0.05);
  padding: 8px;
}

.dropdown-menu:deep(.el-dropdown-menu__item) {
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.2s ease;
}

.dropdown-menu:deep(.el-dropdown-menu__item:hover) {
  background: rgba(0, 184, 148, 0.08);
  color: var(--primary-color);
}

.dropdown-menu:deep(.el-dropdown-menu__item i) {
  font-size: 16px;
}

.auth-btn {
  height: 40px;
  padding: 0 20px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
}

.register-btn {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(0, 184, 148, 0.3);
}

.register-btn:hover {
  background: linear-gradient(135deg, #00a383 0%, #00b5a8 100%);
  box-shadow: 0 6px 20px rgba(0, 184, 148, 0.4);
}

.main {
  flex: 1;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 30px;
}

.footer {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  text-align: center;
  padding: 30px;
  border-top: 1px solid rgba(0, 184, 148, 0.1);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.copyright {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 8px;
  font-weight: 500;
}

.description {
  color: var(--text-muted);
  font-size: 13px;
  letter-spacing: 0.5px;
}

@media (max-width: 1200px) {
  .header-content {
    padding: 0 20px;
    gap: 20px;
  }
  
  .search-bar {
    max-width: 400px;
  }
  
  .main {
    padding: 20px;
  }
}

@media (max-width: 992px) {
  .nav-links {
    gap: 8px;
  }
  
  .nav-btn span {
    display: none;
  }
  
  .nickname {
    display: none;
  }
  
  .dropdown-icon {
    display: none;
  }
}
</style>
