<template>
  <div class="my">
    <el-card>
      <h2>我的商品</h2>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="出售中" name="selling"></el-tab-pane>
        <el-tab-pane label="已卖出" name="sold"></el-tab-pane>
        <el-tab-pane label="已下架" name="offline"></el-tab-pane>
      </el-tabs>
      
      <el-table :data="products" style="width: 100%">
        <el-table-column prop="title" label="商品名称" min-width="200" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            <span class="price">￥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">出售中</el-tag>
            <el-tag v-else-if="row.status === 3" type="warning">已卖出</el-tag>
            <el-tag v-else>已下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/product/${row.id}`)">
              查看
            </el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button
              v-if="row.status === 1"
              size="small"
              type="warning"
              @click="handleTakeOff(row)"
            >
              下架
            </el-button>
            <el-button
              v-if="row.status === 4"
              size="small"
              type="success"
              @click="handlePutOn(row)"
            >
              上架
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadProducts"
        />
      </div>
    </el-card>
    
    <el-dialog v-model="editVisible" title="编辑商品">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="商品标题">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="editForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="editForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="editForm.contact" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getMyProducts, updateProduct, updateProductStatus } from '@/api'
import { ElMessage } from 'element-plus'

const activeTab = ref('selling')
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const editVisible = ref(false)
const editForm = reactive({
  id: null,
  title: '',
  price: 0,
  description: '',
  contact: ''
})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const loadProducts = async () => {
  try {
    const res = await getMyProducts({
      page: currentPage.value - 1,
      size: pageSize.value
    })
    let allProducts = res.data.content || res.data.elements || []
    
    if (activeTab.value === 'selling') {
      products.value = allProducts.filter(p => p.status === 1)
    } else if (activeTab.value === 'sold') {
      products.value = allProducts.filter(p => p.status === 3)
    } else {
      products.value = allProducts.filter(p => p.status !== 1 && p.status !== 3)
    }
    
    total.value = products.value.length
  } catch (e) {
    console.error(e)
  }
}

const handleEdit = (row) => {
  editForm.id = row.id
  editForm.title = row.title
  editForm.price = row.price
  editForm.description = row.description
  editForm.contact = row.contact
  editVisible.value = true
}

const handleUpdate = async () => {
  try {
    await updateProduct(editForm.id, editForm)
    ElMessage.success('修改成功')
    editVisible.value = false
    loadProducts()
  } catch (e) {
    console.error(e)
  }
}

const handleTakeOff = async (row) => {
  try {
    await updateProductStatus(row.id, 4)
    ElMessage.success('已下架')
    loadProducts()
  } catch (e) {
    console.error(e)
  }
}

const handlePutOn = async (row) => {
  try {
    await updateProductStatus(row.id, 1)
    ElMessage.success('已上架')
    loadProducts()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.my {
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
