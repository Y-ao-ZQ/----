<template>
  <div class="my-page">
    <el-card class="my-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2 class="page-title">
            <el-icon><ShoppingBag /></el-icon>
            我的商品
          </h2>
          <el-button type="primary" @click="$router.push('/publish')">
            <el-icon><Plus /></el-icon>
            发布商品
          </el-button>
        </div>
      </template>

      <!-- 标签页切换 -->
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane 
          v-for="tab in tabConfig" 
          :key="tab.name" 
          :label="tab.label" 
          :name="tab.name"
        >
          <template #label>
            <span class="tab-label">
              {{ tab.label }}
              <el-tag v-if="tab.count !== null" :type="tab.tagType" size="small" effect="plain">
                {{ tab.count }}
              </el-tag>
            </span>
          </template>
        </el-tab-pane>
      </el-tabs>

      <!-- 商品表格 -->
      <div class="table-container">
        <el-table 
          :data="filteredProducts" 
          style="width: 100%"
          v-loading="loading"
          :header-cell-style="{ background: '#f5f7fa', color: '#606266', fontWeight: '600' }"
          cell-class-name="table-cell"
        >
          <el-table-column type="index" label="序号" width="70" align="center" />
          
          <el-table-column prop="title" label="商品信息" min-width="280">
            <template #default="{ row }">
              <div class="product-info">
                <el-image 
                  v-if="getFirstImage(row.images)" 
                  :src="getFirstImage(row.images)" 
                  fit="cover"
                  class="product-thumb"
                  :preview-src-list="[getFirstImage(row.images)]"
                >
                  <template #placeholder>
                    <div class="image-loading">
                      <el-icon class="is-loading"><Loading /></el-icon>
                    </div>
                  </template>
                  <template #error>
                    <div class="image-error">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="product-details">
                  <div class="product-title">{{ row.title }}</div>
                  <div class="product-meta">
                    <el-tag size="small" effect="plain">{{ row.category || '未分类' }}</el-tag>
                    <span class="location" v-if="row.location">
                      <el-icon><Location /></el-icon>
                      {{ row.location }}
                    </span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="price" label="价格" width="110" align="right">
            <template #default="{ row }">
              <span class="price">￥{{ formatPrice(row.price) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="viewCount" label="浏览量" width="100" align="center">
            <template #default="{ row }">
              <span class="view-count">
                <el-icon><View /></el-icon>
                {{ row.viewCount || 0 }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column prop="createTime" label="发布时间" width="170" align="center">
            <template #default="{ row }">
              <span class="time-text">{{ formatTime(row.createTime) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="状态" width="110" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" effect="light" size="medium">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="360" fixed="right" align="center">
            <template #default="{ row }">
              <div class="action-buttons">
                <!-- 基础操作按钮组 -->
                <div class="action-group">
                  <el-button 
                    size="small" 
                    @click="handleView(row)"
                    plain
                    class="action-btn"
                  >
                    <el-icon><View /></el-icon>
                    查看
                  </el-button>
                  <el-button 
                    size="small" 
                    type="primary" 
                    @click="handleEdit(row)"
                    plain
                    class="action-btn"
                  >
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                </div>
                
                <!-- 状态操作按钮组 -->
                <div class="action-group status-actions">
                  <!-- 出售中商品操作 -->
                  <template v-if="row.status === PRODUCT_STATUS.SELLING">
                    <el-button 
                      size="small"
                      type="warning" 
                      @click="handleTakeOff(row)"
                      plain
                      class="action-btn"
                    >
                      <el-icon><Download /></el-icon>
                      下架
                    </el-button>
                    <el-button 
                      size="small"
                      type="danger" 
                      @click="handleSold(row)"
                      plain
                      class="action-btn"
                    >
                      <el-icon><Select /></el-icon>
                      卖出
                    </el-button>
                  </template>
                  
                  <!-- 已下架商品操作 -->
                  <template v-else-if="row.status === PRODUCT_STATUS.OFFLINE || row.status === PRODUCT_STATUS.TAKEN_OFF">
                    <el-button 
                      size="small"
                      type="success" 
                      @click="handlePutOn(row)"
                      plain
                      class="action-btn"
                    >
                      <el-icon><Upload /></el-icon>
                      重新上架
                    </el-button>
                    <el-button 
                      size="small"
                      type="danger" 
                      @click="handleDelete(row)"
                      plain
                      class="action-btn"
                    >
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-button>
                  </template>
                  
                  <!-- 已卖出商品操作 -->
                  <template v-else-if="row.status === PRODUCT_STATUS.SOLD_OUT">
                    <el-button 
                      size="small"
                      type="success" 
                      @click="handlePutOn(row)"
                      plain
                      class="action-btn"
                    >
                      <el-icon><Upload /></el-icon>
                      上架
                    </el-button>
                  </template>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 空状态 -->
      <el-empty 
        v-if="!loading && filteredProducts.length === 0" 
        :description="getEmptyDescription()"
        class="empty-state"
      >
        <el-button type="primary" @click="$router.push('/publish')">
          发布第一个商品
        </el-button>
      </el-empty>

      <!-- 分页 -->
      <div class="pagination-container" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 编辑商品对话框 -->
    <el-dialog 
      v-model="editDialog.visible" 
      :title="editDialog.title" 
      width="900px"
      :close-on-click-modal="false"
      @closed="handleEditDialogClosed"
    >
      <el-form 
        ref="editFormRef"
        :model="editForm" 
        :rules="editRules"
        label-width="120px"
        class="edit-form"
      >
        <el-form-item label="商品标题" prop="title">
          <el-input 
            v-model="editForm.title" 
            placeholder="请输入商品标题，突出商品亮点" 
            maxlength="200" 
            show-word-limit 
            clearable
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品分类" prop="category">
              <el-select 
                v-model="editForm.category" 
                placeholder="请选择商品分类" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="cat in categories"
                  :key="cat"
                  :label="cat"
                  :value="cat"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="新旧程度" prop="condition">
              <el-select 
                v-model="editForm.condition" 
                placeholder="请选择" 
                style="width: 100%"
              >
                <el-option label="95 新" value="95 新" />
                <el-option label="9 成新" value="9 成新" />
                <el-option label="8 成新" value="8 成新" />
                <el-option label="7 成新" value="7 成新" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品价格" prop="price">
              <el-input-number 
                v-model="editForm.price" 
                :min="0" 
                :precision="2" 
                :step="0.01"
                :max="999999"
                style="width: 100%"
                placeholder="请输入价格"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contact">
              <el-input 
                v-model="editForm.contact" 
                placeholder="手机号/微信号/QQ" 
                maxlength="50"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="交易地点" prop="location">
          <el-input 
            v-model="editForm.location" 
            placeholder="如：XX 大学 X 号楼" 
            maxlength="100"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="5"
            placeholder="请详细描述商品的规格、使用情况、瑕疵等，让买家更了解商品"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="商品图片" prop="images">
          <el-upload
            ref="uploadRef"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            list-type="picture-card"
            :limit="9"
            accept="image/*"
          >
            <el-icon class="upload-icon"><Plus /></el-icon>
            <template #tip>
              <div class="upload-tip">
                <el-icon><InfoFilled /></el-icon>
                支持 jpg/png/webp/gif 格式，单张不超过 10MB，最多 9 张
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="editDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdate" :loading="editDialog.loading">
            保存修改
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getMyProducts, updateProduct, updateProductStatus, getCategories, deleteProduct } from '@/api'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, 
  ShoppingBag, 
  Loading, 
  Picture, 
  View, 
  Location, 
  InfoFilled,
  Edit,
  Download,
  Upload,
  Delete,
  Select,
  WarningFilled,
  Bell
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 商品状态常量
const PRODUCT_STATUS = {
  OFFLINE: 0,      // 已下架
  SELLING: 1,      // 出售中
  SOLD_OUT: 3,     // 已卖出
  TAKEN_OFF: 4     // 手动下架
}

// 标签页配置
const tabConfig = ref([
  { name: 'selling', label: '出售中', count: null, tagType: 'success' },
  { name: 'sold', label: '已卖出', count: null, tagType: 'warning' },
  { name: 'offline', label: '已下架', count: null, tagType: 'info' }
])

// 响应式数据
const activeTab = ref('selling')
const allProducts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 编辑对话框状态
const editDialog = reactive({
  visible: false,
  title: '编辑商品',
  loading: false
})

// 分类数据
const categories = ref([])

// 文件列表
const fileList = ref([])

// 上传配置
const uploadUrl = '/api/upload/image'
const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${userStore.token}`
}))

// 编辑表单数据
const editForm = reactive({
  id: null,
  title: '',
  category: '',
  price: 0,
  condition: '',
  description: '',
  contact: '',
  location: '',
  images: ''
})

// 表单验证规则
const editRules = {
  title: [
    { required: true, message: '请输入商品标题', trigger: 'blur' },
    { min: 2, max: 200, message: '标题长度在 2 到 200 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于 0', trigger: 'blur' }
  ]
}

// 计算属性：过滤后的商品列表
const filteredProducts = computed(() => {
  const statusMap = {
    selling: [PRODUCT_STATUS.SELLING],
    sold: [PRODUCT_STATUS.SOLD_OUT],
    offline: [PRODUCT_STATUS.OFFLINE, PRODUCT_STATUS.TAKEN_OFF]
  }
  
  const targetStatuses = statusMap[activeTab.value] || []
  return allProducts.value.filter(product => 
    targetStatuses.includes(product.status)
  )
})

// 方法：获取第一张图片
const getFirstImage = (images) => {
  if (!images) return ''
  const imageList = images.split(',').filter(img => img && img.trim())
  return imageList[0] || ''
}

// 方法：格式化价格
const formatPrice = (price) => {
  return Number(price).toFixed(2)
}

// 方法：格式化时间
const formatTime = (time) => {
  if (!time) return '--'
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < 7 * day) {
    return `${Math.floor(diff / day)}天前`
  } else {
    return date.toLocaleDateString('zh-CN')
  }
}

// 方法：获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    [PRODUCT_STATUS.OFFLINE]: '已下架',
    [PRODUCT_STATUS.SELLING]: '出售中',
    [PRODUCT_STATUS.SOLD_OUT]: '已卖出',
    [PRODUCT_STATUS.TAKEN_OFF]: '已下架'
  }
  return statusMap[status] || '未知状态'
}

// 方法：获取状态标签类型
const getStatusType = (status) => {
  const typeMap = {
    [PRODUCT_STATUS.SELLING]: 'success',
    [PRODUCT_STATUS.SOLD_OUT]: 'warning',
    [PRODUCT_STATUS.OFFLINE]: 'info',
    [PRODUCT_STATUS.TAKEN_OFF]: 'info'
  }
  return typeMap[status] || 'info'
}

// 方法：获取空状态描述
const getEmptyDescription = () => {
  const descMap = {
    selling: '暂无出售中的商品',
    sold: '暂无已卖出的商品',
    offline: '暂无已下架的商品'
  }
  return descMap[activeTab.value] || '暂无商品'
}

// 方法：加载商品数据
const loadProducts = async () => {
  loading.value = true
  try {
    const res = await getMyProducts({
      page: currentPage.value - 1,
      size: pageSize.value
    })
    
    allProducts.value = res.data?.content || res.data?.elements || []
    total.value = res.data?.totalElements || allProducts.value.length
    
    // 更新各标签页的数量
    updateTabCounts()
  } catch (error) {
    console.error('加载商品失败:', error)
    ElMessage.error('加载商品失败，请稍后重试')
    allProducts.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 方法：更新标签页数量
const updateTabCounts = () => {
  tabConfig.value[0].count = allProducts.value.filter(
    p => p.status === PRODUCT_STATUS.SELLING
  ).length
  
  tabConfig.value[1].count = allProducts.value.filter(
    p => p.status === PRODUCT_STATUS.SOLD_OUT
  ).length
  
  tabConfig.value[2].count = allProducts.value.filter(
    p => [PRODUCT_STATUS.OFFLINE, PRODUCT_STATUS.TAKEN_OFF].includes(p.status)
  ).length
}

// 方法：加载分类数据
const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
    categories.value = [
      '数码产品', '图书教材', '生活用品', 
      '运动户外', '服饰鞋包', '美妆护肤', '其他'
    ]
  }
}

// 事件处理：标签页切换
const handleTabChange = () => {
  currentPage.value = 1
}

// 事件处理：分页变化
const handlePageChange = () => {
  loadProducts()
}

// 事件处理：每页数量变化
const handleSizeChange = () => {
  currentPage.value = 1
  loadProducts()
}

// 事件处理：查看商品
const handleView = (row) => {
  router.push(`/product/${row.id}`)
}

// 事件处理：编辑商品
const handleEdit = (row) => {
  editForm.id = row.id
  editForm.title = row.title
  editForm.category = row.category || ''
  editForm.price = Number(row.price) || 0
  editForm.condition = row.condition || ''
  editForm.description = row.description || ''
  editForm.contact = row.contact || ''
  editForm.location = row.location || ''
  editForm.images = row.images || ''
  
  // 解析已有图片
  fileList.value = []
  if (row.images) {
    const imageUrls = row.images.split(',').filter(img => img && img.trim())
    fileList.value = imageUrls.map((url, index) => ({
      name: `图片${index + 1}`,
      url: url,
      status: 'success'
    }))
  }
  
  editDialog.visible = true
}

// 事件处理：编辑对话框关闭
const handleEditDialogClosed = () => {
  editForm.id = null
  editForm.title = ''
  editForm.category = ''
  editForm.price = 0
  editForm.condition = ''
  editForm.description = ''
  editForm.contact = ''
  editForm.location = ''
  editForm.images = ''
  fileList.value = []
}

// 方法：上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!')
    return false
  }

  return true
}

// 事件处理：上传成功
const handleUploadSuccess = (response, uploadFile, fileList) => {
  if (response.code === 200) {
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 事件处理：上传失败
const handleUploadError = (error) => {
  console.error('Upload error:', error)
  ElMessage.error('上传失败，请重试')
}

// 事件处理：移除图片
const handleRemove = (file, fileList) => {
  fileList.value = fileList
}

// 事件处理：超出限制
const handleExceed = () => {
  ElMessage.warning('最多只能上传 9 张图片')
}

// 方法：更新商品
const handleUpdate = async () => {
  if (!editForm.title || !editForm.price) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  // 更新图片列表（允许不上传图片）
  editForm.images = fileList.value.map(file => file.url).join(',')
  
  editDialog.loading = true
  try {
    await updateProduct(editForm.id, editForm)
    ElMessage.success('修改成功')
    editDialog.visible = false
    await loadProducts()
  } catch (error) {
    console.error('更新商品失败:', error)
    ElMessage.error('修改失败，请稍后重试')
  } finally {
    editDialog.loading = false
  }
}

// 方法：下架商品
const handleTakeOff = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要下架商品"${row.title}"吗？下架后将不再展示给买家。`,
      '确认下架',
      {
        confirmButtonText: '确定下架',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateProductStatus(row.id, PRODUCT_STATUS.TAKEN_OFF)
    ElMessage.success('已下架')
    await loadProducts()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('下架失败:', error)
      ElMessage.error('下架失败，请稍后重试')
    }
  }
}

// 方法：上架商品
const handlePutOn = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要将商品"${row.title}"重新上架吗？上架后将重新展示给买家。`,
      '确认上架',
      {
        confirmButtonText: '确定上架',
        cancelButtonText: '取消',
        type: 'success'
      }
    )
    
    await updateProductStatus(row.id, PRODUCT_STATUS.SELLING)
    ElMessage.success('已上架')
    await loadProducts()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('上架失败:', error)
      ElMessage.error('上架失败，请稍后重试')
    }
  }
}

// 方法：删除商品
const handleDelete = async (row) => {
  try {
    // 显示简化的确认对话框
    await ElMessageBox.confirm(
      `确定要删除商品"${row.title}"吗？\n\n删除后商品将永久移除且无法恢复，请谨慎操作！`,
      '删除确认',
      {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        confirmButtonClass: 'el-button--danger',
        type: 'warning',
        customClass: 'delete-confirmation-dialog'
      }
    )
    
    // 执行删除操作
    const result = await deleteProduct(row.id)
    console.log('删除结果:', result)
    
    // 显示成功提示
    ElMessage.success({
      message: '商品已成功删除',
      type: 'success',
      duration: 2000
    })
    
    // 刷新列表
    await loadProducts()
    
  } catch (error) {
    // 用户取消删除
    if (error === 'cancel' || error.message === 'cancel') {
      ElMessage.info('已取消删除操作')
      return
    }
    
    // 处理删除失败
    console.error('删除失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '删除失败，请稍后重试'
    
    ElMessage.error({
      message: errorMsg,
      duration: 3000
    })
  }
}

// 方法：标记为卖出
const handleSold = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要将商品"${row.title}"标记为已卖出吗？`,
      '确认卖出',
      {
        confirmButtonText: '确定卖出',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateProductStatus(row.id, PRODUCT_STATUS.SOLD_OUT)
    ElMessage.success('已标记为卖出')
    await loadProducts()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('标记卖出失败:', error)
      ElMessage.error('操作失败，请稍后重试')
    }
  }
}

// 生命周期
onMounted(() => {
  loadProducts()
  loadCategories()
})
</script>

<style scoped>
.my-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.my-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
}

.page-title .el-icon {
  font-size: 24px;
  color: var(--el-color-primary);
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.table-container {
  margin-top: 16px;
  border-radius: 8px;
  overflow: hidden;
}

/* 表格单元格样式 */
:deep(.table-cell) {
  padding: 12px 0;
  vertical-align: middle;
}

/* 表头样式 */
:deep(.el-table th.el-table__cell) {
  font-size: 14px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

/* 表格行样式 */
:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.product-thumb {
  width: 90px;
  height: 90px;
  border-radius: 8px;
  flex-shrink: 0;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.product-thumb:hover {
  transform: scale(1.08);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.image-loading,
.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: #f5f7fa;
  font-size: 24px;
  color: #c0c4cc;
}

.product-details {
  flex: 1;
  min-width: 0;
}

.product-title {
  font-weight: 600;
  font-size: 15px;
  color: var(--el-text-color-primary);
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.6;
}

.product-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.price {
  color: var(--el-color-danger);
  font-weight: 700;
  font-size: 17px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.view-count {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  color: var(--el-text-color-regular);
  font-size: 14px;
  font-weight: 500;
}

.time-text {
  color: var(--el-text-color-regular);
  font-size: 13px;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.action-group {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.action-group.status-actions {
  padding-top: 10px;
  border-top: 1px dashed #dcdfe6;
  margin-top: 6px;
}

.action-btn {
  min-width: 75px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-btn .el-icon {
  font-size: 16px;
}

/* 按钮悬停效果增强 */
.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* 不同状态按钮的颜色强调 */
.action-btn[type="success"]:hover {
  background-color: var(--el-color-success-light-9);
  border-color: var(--el-color-success);
}

.action-btn[type="warning"]:hover {
  background-color: var(--el-color-warning-light-9);
  border-color: var(--el-color-warning);
}

.action-btn[type="danger"]:hover {
  background-color: var(--el-color-danger-light-9);
  border-color: var(--el-color-danger);
}

.action-btn[type="primary"]:hover {
  background-color: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary);
}

.empty-state {
  padding: 60px 20px;
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.edit-form {
  padding: 10px 0;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-tip {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-top: 8px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 删除确认对话框样式 */
:deep(.delete-confirmation-dialog) {
  max-width: 560px;
}

:deep(.delete-confirmation-dialog .el-message-box__header) {
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.delete-confirmation-dialog .el-message-box__content) {
  padding: 24px;
}

:deep(.delete-confirmation-dialog .el-message-box__footer) {
  padding: 16px 24px;
  border-top: 1px solid #ebeef5;
}

:deep(.delete-confirmation-dialog .el-button--danger) {
  background-color: #F56C6C;
  border-color: #F56C6C;
}

:deep(.delete-confirmation-dialog .el-button--danger:hover) {
  background-color: #f78989;
  border-color: #f78989;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .my-page {
    padding: 10px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .product-thumb {
    width: 60px;
    height: 60px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>
