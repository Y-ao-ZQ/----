<template>
  <div class="admin">
    <el-card>
      <h2>后台管理</h2>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="数据统计" name="stats"></el-tab-pane>
        <el-tab-pane label="用户管理" name="users"></el-tab-pane>
        <el-tab-pane label="商品管理" name="products"></el-tab-pane>
      </el-tabs>
      
      <div v-if="activeTab === 'stats'" class="stats">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="用户总数" :value="stats.userCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="商品总数" :value="stats.productCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="在售商品" :value="stats.activeProductCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="已售出" :value="stats.soldProductCount" />
          </el-col>
        </el-row>
      </div>
      
      <div v-if="activeTab === 'users'" class="user-management">
        <el-table :data="users" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="nickname" label="昵称" />
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column label="角色" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.role === 1" type="danger">管理员</el-tag>
              <el-tag v-else>普通用户</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.status === 1" type="success">正常</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button
                v-if="row.status === 1"
                size="small"
                type="warning"
                @click="handleToggleUserStatus(row, 0)"
              >
                禁用
              </el-button>
              <el-button
                v-else
                size="small"
                type="success"
                @click="handleToggleUserStatus(row, 1)"
              >
                启用
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div v-if="activeTab === 'products'" class="product-management">
        <el-table :data="products" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="商品名称" min-width="200" />
          <el-table-column prop="price" label="价格" width="100">
            <template #default="{ row }">
              ￥{{ row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="sellerName" label="卖家" width="120" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.status === 1" type="success">出售中</el-tag>
              <el-tag v-else-if="row.status === 3" type="warning">已售出</el-tag>
              <el-tag v-else type="info">已下架</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间" width="180">
            <template #default="{ row }">
              {{ formatTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="$router.push(`/product/${row.id}`)">
                查看
              </el-button>
              <el-button size="small" type="danger" @click="handleDeleteProduct(row.id)">
                下架
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getStats, getUsers, updateUserStatus, getAdminProducts, deleteAdminProduct } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('stats')
const stats = reactive({
  userCount: 0,
  productCount: 0,
  activeProductCount: 0,
  soldProductCount: 0
})
const users = ref([])
const products = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadStats = async () => {
  try {
    const res = await getStats()
    const data = res.data
    stats.userCount = data.userCount
    stats.productCount = data.productCount
    stats.activeProductCount = data.activeProductCount
    stats.soldProductCount = data.soldProductCount
  } catch (e) {
    console.error(e)
  }
}

const loadUsers = async () => {
  try {
    const res = await getUsers({ page: 0, size: 100 })
    users.value = res.data.content || res.data.elements || []
  } catch (e) {
    console.error(e)
  }
}

const loadProducts = async () => {
  try {
    const res = await getAdminProducts({ page: 0, size: 100 })
    products.value = res.data.content || res.data.elements || []
  } catch (e) {
    console.error(e)
  }
}

const handleToggleUserStatus = async (user, status) => {
  try {
    await updateUserStatus(user.id, status)
    ElMessage.success('操作成功')
    loadUsers()
  } catch (e) {
    console.error(e)
  }
}

const handleDeleteProduct = async (productId) => {
  try {
    await ElMessageBox.confirm('确认下架该商品吗？', '提示', { type: 'warning' })
    await deleteAdminProduct(productId)
    ElMessage.success('已下架')
    loadProducts()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadStats()
  loadUsers()
  loadProducts()
})
</script>

<style scoped>
.admin {
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.stats {
  padding: 20px 0;
}

.user-management,
.product-management {
  margin-top: 20px;
}
</style>
