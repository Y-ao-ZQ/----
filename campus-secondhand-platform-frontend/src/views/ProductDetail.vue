<template>
  <div class="product-detail">
    <el-card>
      <div class="product-content">
        <div class="product-images">
          <div v-if="imageList.length > 0">
            <el-image
              :src="currentImage"
              fit="cover"
              class="main-image"
              :preview-src-list="imageList"
              :initial-index="0"
              @click="handleImagePreview"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                  <span>图片加载失败</span>
                </div>
              </template>
            </el-image>
            
            <!-- 缩略图列表 -->
            <div v-if="imageList.length > 1" class="thumbnail-list">
              <div
                v-for="(img, index) in imageList"
                :key="index"
                :class="['thumbnail-item', { active: currentImage === img }]"
                @click="currentImage = img"
              >
                <el-image :src="img" fit="cover" class="thumbnail" />
              </div>
            </div>
          </div>
          
          <div v-else class="image-error">
            <el-icon><Picture /></el-icon>
            <span>暂无图片</span>
          </div>
        </div>
        
        <div class="product-info">
          <h1 class="product-title">{{ product.title }}</h1>
          <div class="product-price">￥{{ product.price }}</div>
          
          <div class="product-meta">
            <div class="meta-item">
              <span class="label">卖家：</span>
              <span>{{ product.sellerName }}</span>
            </div>
            <div class="meta-item">
              <span class="label">浏览量：</span>
              <span>{{ product.viewCount }}</span>
            </div>
            <div class="meta-item">
              <span class="label">发布时间：</span>
              <span>{{ formatTime(product.createTime) }}</span>
            </div>
            <div class="meta-item">
              <span class="label">位置：</span>
              <span>{{ product.location || '未填写' }}</span>
            </div>
          </div>
          
          <div class="product-actions">
            <el-button type="primary" size="large" @click="handleContact">
              <el-icon><ChatLineRound /></el-icon>
              联系我
            </el-button>
            <el-button :type="isFavorited ? 'success' : 'default'" @click="handleFavorite">
              <el-icon><Star v-if="isFavorited" /><StarFilled v-else /></el-icon>
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
            <el-button type="warning" size="large" @click="handleBuy">
              <el-icon><ShoppingCart /></el-icon>
              我想要
            </el-button>
          </div>
        </div>
      </div>
      
      <div class="product-description">
        <h3>商品描述</h3>
        <p>{{ product.description || '暂无描述' }}</p>
      </div>
      
      <div class="product-specs">
        <h3>商品规格</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="商品分类">{{ product.category || '未分类' }}</el-descriptions-item>
          <el-descriptions-item label="新旧程度">{{ product.condition || '未填写' }}</el-descriptions-item>
          <el-descriptions-item label="图片数量">{{ imageList.length }}张</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    
    <el-dialog v-model="contactVisible" title="联系方式">
      <p>卖家联系方式：<strong>{{ product.contact || '未填写' }}</strong></p>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getProductById, addFavorite, removeFavorite, checkFavorite, createOrder } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const product = ref({})
const isFavorited = ref(false)
const contactVisible = ref(false)
const currentImage = ref('')

// 解析图片列表
const imageList = computed(() => {
  if (!product.value.images) return []
  return product.value.images.split(',').filter(img => img && img.trim())
})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const handleImagePreview = () => {
  // El-image 自带预览功能，这里可以添加自定义逻辑
}

const loadProduct = async () => {
  try {
    const res = await getProductById(route.params.id)
    product.value = res.data
    if (imageList.value.length > 0) {
      currentImage.value = imageList.value[0]
    }
  } catch (e) {
    console.error(e)
  }
}

const checkFavoriteStatus = async () => {
  if (!userStore.token) return
  
  try {
    const res = await checkFavorite(route.params.id)
    isFavorited.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const handleFavorite = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  try {
    if (isFavorited.value) {
      await removeFavorite(route.params.id)
      isFavorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(route.params.id)
      isFavorited.value = true
      ElMessage.success('收藏成功')
    }
  } catch (e) {
    console.error(e)
  }
}

const handleContact = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  contactVisible.value = true
}

const handleBuy = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (product.value.sellerId === userStore.userId) {
    ElMessage.warning('不能购买自己的商品')
    return
  }
  
  try {
    await createOrder({ productId: product.value.id })
    ElMessage.success('下单成功，请在订单中查看')
    router.push('/orders')
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadProduct()
  checkFavoriteStatus()
})
</script>

<style scoped>
.product-detail {
  max-width: 1000px;
  margin: 0 auto;
}

.product-content {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
}

.product-images {
  flex: 1;
  max-width: 450px;
}

.main-image {
  width: 100%;
  height: 450px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.main-image:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  flex-wrap: wrap;
}

.thumbnail-item {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.thumbnail-item:hover {
  border-color: var(--el-color-primary);
  transform: translateY(-2px);
}

.thumbnail-item.active {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.thumbnail {
  width: 100%;
  height: 100%;
}

.image-error {
  width: 100%;
  height: 450px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
  color: #999;
  font-size: 40px;
  border-radius: 8px;
}

.image-error span {
  font-size: 14px;
  margin-top: 10px;
}

.product-info {
  flex: 1;
}

.product-title {
  font-size: 24px;
  margin-bottom: 15px;
}

.product-price {
  font-size: 28px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 20px;
}

.product-meta {
  margin-bottom: 30px;
}

.meta-item {
  display: flex;
  margin-bottom: 10px;
  font-size: 14px;
}

.meta-item .label {
  color: #999;
  width: 80px;
}

.product-actions {
  display: flex;
  gap: 15px;
}

.product-description,
.product-specs {
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid #eee;
}

.product-description h3,
.product-specs h3 {
  margin-bottom: 15px;
  font-size: 18px;
}

.product-description p {
  line-height: 1.8;
  color: #666;
}

/* 图片预览样式 */
:deep(.el-image-viewer__wrapper) {
  max-height: 90vh;
}
</style>
