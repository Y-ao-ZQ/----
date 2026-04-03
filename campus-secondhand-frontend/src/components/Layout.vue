<template>
  <div class="layout">
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <el-icon><ShoppingCart /></el-icon>
          <span>校园二手</span>
        </div>
        
        <div class="search-bar">
          <el-input
            v-model="keyword"
            placeholder="搜索商品"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="nav-links">
          <el-button text @click="$router.push('/publish')">
            <el-icon><Plus /></el-icon>
            发布
          </el-button>
          
          <el-button text @click="$router.push('/messages')" style="position: relative;">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0">
              <el-icon><ChatDotRound /></el-icon>
            </el-badge>
            <span style="margin-left: 4px;">消息</span>
          </el-button>
          
          <el-button text @click="$router.push('/orders')">
            <el-icon><List /></el-icon>
            订单
          </el-button>
          
          <el-button text @click="$router.push('/my')">
            <el-icon><User /></el-icon>
            我的
          </el-button>
          
          <template v-if="userStore.token">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" :src="userStore.avatar || ''" />
                <span class="nickname">{{ userStore.nickname || userStore.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
                  <el-dropdown-item v-if="userStore.role === 1" command="admin">后台管理</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          
          <template v-else>
            <el-button text @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    
    <el-main class="main">
      <router-view />
    </el-main>
    
    <el-footer class="footer">
      <p>校园二手交易平台 © 2024</p>
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
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
  margin-right: 40px;
}

.search-bar {
  flex: 1;
  max-width: 400px;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.nickname {
  font-size: 14px;
}

.main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 20px;
}

.footer {
  background: #fff;
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 14px;
}
</style>
