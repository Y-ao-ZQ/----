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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories, createProduct } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const categories = ref([])

const form = reactive({
  title: '',
  category: '',
  price: 0,
  condition: '',
  description: '',
  contact: '',
  location: ''
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
</style>
