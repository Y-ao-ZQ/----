import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const avatar = ref(localStorage.getItem('avatar') || '')
  const role = ref(parseInt(localStorage.getItem('role') || '0'))

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUserInfo(info) {
    userId.value = info.userId
    username.value = info.username
    nickname.value = info.nickname
    avatar.value = info.avatar
    role.value = info.role
    
    localStorage.setItem('userId', info.userId)
    localStorage.setItem('username', info.username)
    localStorage.setItem('nickname', info.nickname)
    localStorage.setItem('avatar', info.avatar)
    localStorage.setItem('role', info.role)
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    nickname.value = ''
    avatar.value = ''
    role.value = 0
    
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('nickname')
    localStorage.removeItem('avatar')
    localStorage.removeItem('role')
  }

  return {
    token,
    userId,
    username,
    nickname,
    avatar,
    role,
    setToken,
    setUserInfo,
    logout
  }
})
