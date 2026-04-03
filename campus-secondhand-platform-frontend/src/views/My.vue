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
        <el-table-column prop="title" label="商品名称" min-width="200">
          <template #default="{ row }">
            <div class="product-title-cell">
              <el-image 
                v-if="row.images && row.images.split(',')[0]" 
                :src="row.images.split(',')[0]" 
                fit="cover"
                class="product-thumb"
              />
              <span>{{ row.title }}</span>
            </div>
          </template>
        </el-table-column>
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
    
    <!-- 编辑商品对话框 -->
    <el-dialog v-model="editVisible" title="编辑商品" width="800px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="商品标题">
          <el-input v-model="editForm.title" placeholder="请输入商品标题" maxlength="200" show-word-limit />
        </el-form-item>
        
        <el-form-item label="商品分类">
          <el-select v-model="editForm.category" placeholder="请选择商品分类" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat"
              :label="cat"
              :value="cat"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="商品价格">
          <el-input-number v-model="editForm.price" :min="0" :precision="2" :step="1" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="新旧程度">
          <el-select v-model="editForm.condition" placeholder="请选择新旧程度" style="width: 100%">
            <el-option label="95 新" value="95 新" />
            <el-option label="9 成新" value="9 成新" />
            <el-option label="8 成新" value="8 成新" />
            <el-option label="7 成新" value="7 成新" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="商品图片">
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
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 jpg/png/webp/gif 文件，且不超过 10MB，最多 9 张
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="商品描述">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="4"
            placeholder="请详细描述商品的规格、使用情况、瑕疵等"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="联系方式">
          <el-input v-model="editForm.contact" placeholder="手机号/微信号/QQ 号" maxlength="50" />
        </el-form-item>
        
        <el-form-item label="交易地点">
          <el-input v-model="editForm.location" placeholder="如：XX 大学 X 号楼" maxlength="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate" :loading="updating">保存</el-button>
      </template>
    </el-dialog>
    
    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图片预览" width="800px">
      <img :src="previewImage" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getMyProducts, updateProduct, updateProductStatus, getCategories } from '@/api'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const userStore = useUserStore()
const activeTab = ref('selling')
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const editVisible = ref(false)
const updating = ref(false)
const previewVisible = ref(false)
const previewImage = ref('')
const categories = ref([])
const fileList = ref([])

const uploadUrl = '/api/upload/image'

const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${userStore.token}`
}))

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

const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const handleEdit = (row) => {
  editForm.id = row.id
  editForm.title = row.title
  editForm.category = row.category || ''
  editForm.price = row.price
  editForm.condition = row.condition || ''
  editForm.description = row.description
  editForm.contact = row.contact
  editForm.location = row.location || ''
  editForm.images = row.images || ''
  
  // 解析已有图片
  fileList.value = []
  if (row.images) {
    const imageUrls = row.images.split(',').filter(img => img && img.trim())
    fileList.value = imageUrls.map((url, index) => ({
      name: `图片${index + 1}`,
      url: url
    }))
  }
  
  editVisible.value = true
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!')
  }

  return isImage && isLt10M
}

const handleUploadSuccess = (response, uploadFile) => {
  if (response.code === 200) {
    ElMessage.success('上传成功')
    updateImagesField()
  } else {
    ElMessage.error(response.message || '上传失败')
    const index = fileList.value.findIndex(f => f.uid === uploadFile.uid)
    if (index !== -1) {
      fileList.value.splice(index, 1)
    }
  }
}

const handleUploadError = (error) => {
  console.error('Upload error:', error)
  ElMessage.error('上传失败，请重试')
}

const handleRemove = (file) => {
  const index = fileList.value.findIndex(f => f.uid === file.uid)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
  updateImagesField()
}

const handleExceed = () => {
  ElMessage.warning('最多只能上传 9 张图片')
}

const updateImagesField = () => {
  editForm.images = fileList.value.map(file => file.url).join(',')
}

const handleUpdate = async () => {
  if (!editForm.title || !editForm.price) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  if (fileList.value.length === 0) {
    ElMessage.warning('请至少上传一张商品图片')
    return
  }
  
  updating.value = true
  try {
    await updateProduct(editForm.id, editForm)
    ElMessage.success('修改成功')
    editVisible.value = false
    loadProducts()
  } catch (e) {
    console.error(e)
  } finally {
    updating.value = false
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
  loadCategories()
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

.product-title-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-thumb {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  flex-shrink: 0;
}

.el-upload__tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

:deep(.el-upload-list__item) {
  transition: all 0.3s;
}

:deep(.el-upload-list__item:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>
