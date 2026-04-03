<template>
  <div class="profile">
    <el-card>
      <h2>个人中心</h2>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="个人资料" name="profile"></el-tab-pane>
        <el-tab-pane label="修改密码" name="password"></el-tab-pane>
      </el-tabs>
      
      <el-form v-if="activeTab === 'profile'" :model="profileForm" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="profileForm.username" disabled />
        </el-form-item>
        
        <el-form-item label="昵称">
          <el-input v-model="profileForm.nickname" />
        </el-form-item>
        
        <el-form-item label="性别">
          <el-radio-group v-model="profileForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
            <el-radio label="保密">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="手机号">
          <el-input v-model="profileForm.phone" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="profileForm.email" />
        </el-form-item>
        
        <el-form-item label="学校">
          <el-input v-model="profileForm.school" />
        </el-form-item>
        
        <el-form-item label="学院">
          <el-input v-model="profileForm.college" />
        </el-form-item>
        
        <el-form-item label="年级">
          <el-input v-model="profileForm.grade" placeholder="如：2021 级" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdateProfile">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
      
      <el-form v-if="activeTab === 'password'" :model="passwordForm" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdatePassword">
            修改密码
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUserInfo, updatePassword } from '@/api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('profile')
const loading = ref(false)

const profileForm = reactive({
  username: '',
  nickname: '',
  gender: '保密',
  phone: '',
  email: '',
  school: '',
  college: '',
  grade: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    const user = res.data
    profileForm.username = user.username
    profileForm.nickname = user.nickname || ''
    profileForm.gender = user.gender || '保密'
    profileForm.phone = user.phone || ''
    profileForm.email = user.email || ''
    profileForm.school = user.school || ''
    profileForm.college = user.college || ''
    profileForm.grade = user.grade || ''
  } catch (e) {
    console.error(e)
  }
}

const handleUpdateProfile = async () => {
  loading.value = true
  try {
    await updateUserInfo(profileForm)
    ElMessage.success('修改成功')
    loadUserInfo()
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleUpdatePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  if (passwordForm.newPassword.length < 6) {
    ElMessage.error('密码长度不能少于 6 位')
    return
  }
  
  loading.value = true
  try {
    await updatePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    userStore.logout()
    setTimeout(() => {
      window.location.href = '/login'
    }, 1000)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}
</style>
