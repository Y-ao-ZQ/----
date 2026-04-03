<template>
  <div class="messages">
    <el-card>
      <h2>消息中心</h2>
      
      <div class="message-list">
        <div v-for="msg in messages" :key="msg.id" class="message-item" :class="{ unread: msg.status === 0 }">
          <div class="message-header">
            <span class="sender">{{ msg.senderName }}</span>
            <span class="time">{{ formatTime(msg.createTime) }}</span>
          </div>
          <div class="message-content">{{ msg.content }}</div>
          <div class="message-footer">
            <span v-if="msg.productTitle" class="product-info">
              关于商品：{{ msg.productTitle }}
            </span>
            <el-button
              v-if="msg.status === 0 && msg.senderId !== userId"
              size="small"
              @click="handleMarkAsRead(msg.id)"
            >
              标记为已读
            </el-button>
          </div>
        </div>
      </div>
      
      <div v-if="messages.length === 0" class="empty">
        <el-empty description="暂无消息" />
      </div>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadMessages"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getReceivedMessages, markAsRead } from '@/api'

const userStore = useUserStore()
const messages = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadMessages = async () => {
  try {
    const res = await getReceivedMessages({
      page: currentPage.value - 1,
      size: pageSize.value
    })
    messages.value = res.data.content || res.data.elements || []
    total.value = res.data.totalElements || 0
  } catch (e) {
    console.error(e)
  }
}

const handleMarkAsRead = async (messageId) => {
  try {
    await markAsRead(messageId)
    loadMessages()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.messages {
  max-width: 1000px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.message-list {
  margin-top: 20px;
}

.message-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  background: #fff;
}

.message-item.unread {
  background: #f0f9ff;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.sender {
  font-weight: 500;
  color: #409EFF;
}

.time {
  color: #999;
  font-size: 13px;
}

.message-content {
  margin-bottom: 10px;
  line-height: 1.6;
}

.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-info {
  color: #666;
  font-size: 13px;
}

.empty {
  padding: 50px 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
