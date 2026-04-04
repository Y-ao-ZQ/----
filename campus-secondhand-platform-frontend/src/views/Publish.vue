<template>
  <div class="publish">
    <el-card>
      <h2>发布商品</h2>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入商品标题" maxlength="200" show-word-limit />
        </el-form-item>
        
        <el-form-item label="商品分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择商品分类" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat"
              :label="cat"
              :value="cat"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="新旧程度" prop="condition">
          <el-select v-model="form.condition" placeholder="请选择新旧程度" style="width: 100%">
            <el-option label="95 新" value="95 新" />
            <el-option label="9 成新" value="9 成新" />
            <el-option label="8 成新" value="8 成新" />
            <el-option label="7 成新" value="7 成新" />
            <el-option label="其他" value="其他" />
          </el-select>
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
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 jpg/png/webp/gif 文件，且不超过 10MB，最多 9 张
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请详细描述商品的规格、使用情况、瑕疵等"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="手机号/微信号/QQ 号" maxlength="50" />
        </el-form-item>
        
        <el-form-item label="交易地点" prop="location">
          <el-input v-model="form.location" placeholder="如：XX 大学 X 号楼" maxlength="100" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            立即发布
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 图片预览对话框 -->
    <el-dialog v-model="dialogVisible" title="图片预览" width="800px">
      <img :src="previewImage" style="width: 100%" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories, createProduct } from '@/api'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const uploadRef = ref(null)
const loading = ref(false)
const categories = ref([])
const fileList = ref([])
const dialogVisible = ref(false)
const previewImage = ref('')

const uploadUrl = '/api/upload/image'

const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${userStore.token}`
}))

const form = reactive({
  title: '',
  category: '',
  price: 0,
  condition: '',
  description: '',
  contact: '',
  location: '',
  images: ''
})

const rules = {
  title: [
    { required: true, message: '请输入商品标题', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' }
  ]
}

const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error(e)
  }
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
    // 上传失败时从文件列表中移除
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
  // 从文件列表中移除
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
  // 将所有图片 URL 用逗号分隔存储
  form.images = fileList.value.map(file => file.url).join(',')
}

const handlePictureCardPreview = (file) => {
  previewImage.value = file.url
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await createProduct(form)
        ElMessage.success('发布成功')
        router.push('/my')
      } catch (e) {
        console.error(e)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleReset = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  fileList.value = []
  form.images = ''
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.publish {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 30px;
  text-align: center;
}

:deep(.el-upload-list__item) {
  transition: all 0.3s;
}

:deep(.el-upload-list__item:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.el-upload__tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
