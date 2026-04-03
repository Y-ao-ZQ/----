<template>
  <div class="favorites">
    <el-card>
      <h2>我的收藏</h2>
      
      <div v-if="favorites.length === 0" class="empty">
        <el-empty description="暂无收藏" />
      </div>
      
      <div v-else class="product-list">
        <div v-for="fav in favoriteProducts" :key="fav.productId" class="product-card">
          <div class="product-image" @click="goToDetail(fav.productId)">
            <el-image
              :src="getProductImage(fav.productId)"
              fit="cover"
              class="product-img"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          
          <div class="product-info">
            <div class="product-title" @click="goToDetail(fav.productId)">
              {{ getProductTitle(fav.productId) }}
            </div>
            <div class="product-price">￥{{ getProductPrice(fav.productId) }}</div>
            <div class="product-footer">
              <el-button size="small" @click="goToDetail(fav.productId)">
                查看详情
              </el-button>
              <el-button size="small" type="danger" @click="handleRemove(fav)">
                取消收藏
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavorites, removeFavorite, getProductById } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const favorites = ref([])
const favoriteProducts = ref([])

const getProductImage = (productId) => {
  const product = favoriteProducts.value.find(p => p.id === productId)
  return product?.images ? product.images.split(',')[0] : ''
}

const getProductTitle = (productId) => {
  const product = favoriteProducts.value.find(p => p.id === productId)
  return product?.title || ''
}

const getProductPrice = (productId) => {
  const product = favoriteProducts.value.find(p => p.id === productId)
  return product?.price || 0
}

const loadFavorites = async () => {
  try {
    const res = await getFavorites()
    favorites.value = res.data
    
    for (const fav of favorites.value) {
      try {
        const productRes = await getProductById(fav.productId)
        favoriteProducts.value.push(productRes.data)
      } catch (e) {
        console.error('加载商品失败:', e)
      }
    }
  } catch (e) {
    console.error(e)
  }
}

const handleRemove = async (fav) => {
  try {
    await removeFavorite(fav.productId)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (e) {
    console.error(e)
  }
}

const goToDetail = (productId) => {
  router.push(`/product/${productId}`)
}

onMounted(() => {
  loadFavorites()
})
</script>

<style scoped>
.favorites {
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.empty {
  padding: 50px 0;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  overflow: hidden;
  background: #f5f5f5;
}

.product-img {
  width: 100%;
  height: 100%;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 40px;
  color: #ccc;
}

.product-info {
  padding: 15px;
}

.product-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 10px;
}

.product-footer {
  display: flex;
  gap: 10px;
}
</style>
