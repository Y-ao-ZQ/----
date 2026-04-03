<template>
  <div class="orders">
    <el-card>
      <h2>我的订单</h2>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="待付款" name="pending"></el-tab-pane>
        <el-tab-pane label="待发货" name="paid"></el-tab-pane>
        <el-tab-pane label="已完成" name="finished"></el-tab-pane>
        <el-tab-pane label="已取消" name="cancelled"></el-tab-pane>
      </el-tabs>
      
      <div class="order-list">
        <div v-for="order in filteredOrders" :key="order.id" class="order-item">
          <div class="order-header">
            <span>订单号：{{ order.orderNo }}</span>
            <span>创建时间：{{ formatTime(order.createTime) }}</span>
          </div>
          
          <div class="order-product">
            <div class="product-info">
              <div class="product-title">{{ order.productTitle }}</div>
              <div class="product-price">￥{{ order.price }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-status">
              <el-tag v-if="order.status === 0">待付款</el-tag>
              <el-tag v-else-if="order.status === 1" type="warning">待发货</el-tag>
              <el-tag v-else-if="order.status === 2" type="success">已完成</el-tag>
              <el-tag v-else-if="order.status === -1" type="info">已取消</el-tag>
            </div>
            
            <div class="order-actions">
              <el-button size="small" @click="viewDetail(order)">
                查看详情
              </el-button>
              
              <el-button
                v-if="order.status === 0 && order.buyerId === userId"
                size="small"
                type="primary"
                @click="handlePay(order.id)"
              >
                确认付款
              </el-button>
              
              <el-button
                v-if="order.status === 0 && order.buyerId === userId"
                size="small"
                type="danger"
                @click="handleCancel(order.id)"
              >
                取消订单
              </el-button>
              
              <el-button
                v-if="order.status === 1 && order.sellerId === userId"
                size="small"
                type="success"
                @click="handleFinish(order.id)"
              >
                确认发货
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="filteredOrders.length === 0" class="empty">
        <el-empty description="暂无订单" />
      </div>
    </el-card>
    
    <el-dialog v-model="detailVisible" title="订单详情">
      <el-descriptions :column="1" border v-if="currentOrder">
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentOrder.productTitle }}</el-descriptions-item>
        <el-descriptions-item label="价格">￥{{ currentOrder.price }}</el-descriptions-item>
        <el-descriptions-item label="买家">{{ currentOrder.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="卖家">{{ currentOrder.sellerName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentOrder.status === 0">待付款</el-tag>
          <el-tag v-else-if="currentOrder.status === 1" type="warning">待发货</el-tag>
          <el-tag v-else-if="currentOrder.status === 2" type="success">已完成</el-tag>
          <el-tag v-else-if="currentOrder.status === -1" type="info">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentOrder.remark || '无' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(currentOrder.createTime) }}</el-descriptions-item>
        <el-descriptions-item v-if="currentOrder.payTime" label="付款时间">{{ formatTime(currentOrder.payTime) }}</el-descriptions-item>
        <el-descriptions-item v-if="currentOrder.finishTime" label="完成时间">{{ formatTime(currentOrder.finishTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { getBuyOrders, getSellOrders, payOrder, cancelOrder, finishOrder } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const userId = userStore.userId
const activeTab = ref('all')
const buyOrders = ref([])
const sellOrders = ref([])
const detailVisible = ref(false)
const currentOrder = ref(null)

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadOrders = async () => {
  try {
    const buyRes = await getBuyOrders({ page: 0, size: 100 })
    buyOrders.value = buyRes.data.content || buyRes.data.elements || []
    
    const sellRes = await getSellOrders({ page: 0, size: 100 })
    sellOrders.value = sellRes.data.content || sellRes.data.elements || []
  } catch (e) {
    console.error(e)
  }
}

const filteredOrders = computed(() => {
  let allOrders = [...buyOrders.value, ...sellOrders.value]
  
  if (activeTab.value === 'pending') {
    return allOrders.filter(o => o.status === 0)
  } else if (activeTab.value === 'paid') {
    return allOrders.filter(o => o.status === 1)
  } else if (activeTab.value === 'finished') {
    return allOrders.filter(o => o.status === 2)
  } else if (activeTab.value === 'cancelled') {
    return allOrders.filter(o => o.status === -1)
  }
  
  return allOrders.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
})

const viewDetail = (order) => {
  currentOrder.value = order
  detailVisible.value = true
}

const handlePay = async (orderId) => {
  try {
    await ElMessageBox.confirm('确认付款吗？', '提示', { type: 'warning' })
    await payOrder(orderId)
    ElMessage.success('付款成功')
    loadOrders()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

const handleCancel = async (orderId) => {
  try {
    await ElMessageBox.confirm('确认取消订单吗？', '提示', { type: 'warning' })
    await cancelOrder(orderId)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

const handleFinish = async (orderId) => {
  try {
    await ElMessageBox.confirm('确认发货吗？', '提示', { type: 'warning' })
    await finishOrder(orderId)
    ElMessage.success('订单已完成')
    loadOrders()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders {
  max-width: 1000px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.order-list {
  margin-top: 20px;
}

.order-item {
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 15px;
  overflow: hidden;
}

.order-header {
  background: #f5f5f5;
  padding: 10px 15px;
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #666;
}

.order-product {
  padding: 15px;
}

.product-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-title {
  font-size: 15px;
  font-weight: 500;
}

.product-price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.order-footer {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.empty {
  padding: 50px 0;
}
</style>
