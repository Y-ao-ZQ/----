<template>
  <div class="home">
    <div class="home-header">
      <h1 class="page-title">
        <el-icon><ShoppingBag /></el-icon>
        热门闲置
      </h1>
      <p class="page-subtitle">发现校园里的宝藏好物</p>
    </div>
    
    <div class="categories">
      <div 
        v-for="cat in categories" 
        :key="cat"
        class="category-card"
        :class="{ active: currentCategory === cat }"
        @click="selectCategory(cat)"
      >
        <div class="category-icon">
          <el-icon v-if="cat === '电子产品'"><Cellphone /></el-icon>
          <el-icon v-else-if="cat === '图书'"><Reading /></el-icon>
          <el-icon v-else-if="cat === '服饰'"><TShirt /></el-icon>
          <el-icon v-else-if="cat === '美妆'"><Mirror /></el-icon>
          <el-icon v-else-if="cat === '运动'"><Basketball /></el-icon>
          <el-icon v-else><ShoppingCart /></el-icon>
        </div>
        <span class="category-name">{{ cat }}</span>
      </div>
      <div 
        class="category-card all"
        :class="{ active: currentCategory === '' }"
        @click="selectCategory('')"
      >
        <div class="category-icon">
          <el-icon><Grid /></el-icon>
        </div>
        <span class="category-name">全部</span>
      </div>
    </div>
    
    <div class="product-list">
      <div v-for="product in products" :key="product.id" class="product-card">
        <div class="product-image" @click="goToDetail(product.id)">
          <el-image
            :src="product.images ? product.images.split(',')[0] : ''"
            fit="cover"
            class="product-img"
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
          <div class="image-overlay">
            <el-button type="primary" size="small" circle class="view-btn">
              <el-icon><View /></el-icon>
            </el-button>
          </div>
        </div>
        
        <div class="product-info">
          <div class="product-category">{{ product.category || '闲置好物' }}</div>
          <div class="product-title" @click="goToDetail(product.id)">{{ product.title }}</div>
          <div class="product-price">
            <span class="currency">￥</span>
            <span class="price-value">{{ product.price }}</span>
          </div>
          <div class="product-meta">
            <div class="seller-info">
              <el-icon><User /></el-icon>
              <span>{{ product.sellerName }}</span>
            </div>
            <div class="location">
              <el-icon><Location /></el-icon>
              <span>{{ product.location }}</span>
            </div>
          </div>
          <div class="product-footer">
            <div class="stats">
              <span class="view-count">
                <el-icon><View /></el-icon>
                {{ product.viewCount }}
              </span>
              <span class="like-count">
                <el-icon><Star /></el-icon>
                {{ product.likeCount }}
              </span>
            </div>
            <el-button type="primary" size="small" round @click="goToDetail(product.id)">
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-if="products.length === 0">
      <el-empty description="暂无商品" />
    </div>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        @current-change="loadProducts"
        class="modern-pagination"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProducts, getCategories } from '@/api'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const currentCategory = ref('')
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const loadProducts = async () => {
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    
    if (currentCategory.value) {
      params.category = currentCategory.value
    }
    
    if (route.query.keyword) {
      params.keyword = route.query.keyword
    }
    
    const res = await getProducts(params)
    const data = res.data
    products.value = data.content || data.elements || []
    total.value = data.totalElements || 0
  } catch (e) {
    console.error(e)
  }
}

const selectCategory = (category) => {
  currentCategory.value = category
  currentPage.value = 1
  // 清除搜索关键词，因为分类和搜索是互斥的筛选方式
  if (route.query.keyword) {
    router.replace({ query: {} })
  }
  loadProducts()
}

const goToDetail = (id) => {
  router.push(`/product/${id}`)
}

watch(() => route.query.keyword, () => {
  currentPage.value = 1
  loadProducts()
})

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.home {
  min-height: 500px;
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.home-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, #00b894 0%, #0984e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 10px;
}

.page-title .el-icon {
  font-size: 36px;
  background: linear-gradient(135deg, #00b894 0%, #0984e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 16px;
  color: var(--text-muted);
  font-weight: 400;
}

.categories {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 30px;
  padding: 0 10px;
}

.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 24px;
  background: white;
  border-radius: 16px;
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
  min-width: 100px;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
  border-color: rgba(0, 184, 148, 0.2);
}

.category-card.active {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  color: white;
  box-shadow: 0 8px 24px rgba(0, 184, 148, 0.3);
  transform: translateY(-4px);
}

.category-card.all.active {
  background: linear-gradient(135deg, #0984e3 0%, #6c5ce7 100%);
}

.category-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  border-radius: 12px;
  background: rgba(0, 184, 148, 0.1);
  transition: all 0.3s ease;
}

.category-card.active .category-icon,
.category-card.all.active .category-icon {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.category-card:hover .category-icon {
  background: rgba(0, 184, 148, 0.2);
}

.category-name {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 30px;
}

.product-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--shadow-md);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  border: 2px solid transparent;
  animation: cardFadeIn 0.5s ease;
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.product-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: var(--shadow-xl);
  border-color: rgba(0, 184, 148, 0.2);
}

.product-image {
  height: 240px;
  overflow: hidden;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  position: relative;
}

.product-img {
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease;
}

.product-card:hover .product-img {
  transform: scale(1.1);
}

.image-loading,
.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 48px;
  color: var(--text-muted);
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-card:hover .image-overlay {
  opacity: 1;
}

.view-btn {
  transform: translateY(20px);
  transition: transform 0.3s ease;
}

.product-card:hover .view-btn {
  transform: translateY(0);
}

.product-info {
  padding: 20px;
}

.product-category {
  font-size: 12px;
  color: var(--primary-color);
  background: rgba(0, 184, 148, 0.1);
  padding: 4px 10px;
  border-radius: 6px;
  display: inline-block;
  margin-bottom: 10px;
  font-weight: 500;
}

.product-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color 0.3s ease;
  cursor: pointer;
}

.product-title:hover {
  color: var(--primary-color);
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 15px;
}

.currency {
  font-size: 16px;
  color: #ff7675;
  font-weight: 600;
}

.price-value {
  font-size: 26px;
  color: #ff7675;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  margin-bottom: 15px;
}

.seller-info,
.location {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}

.seller-info .el-icon,
.location .el-icon {
  font-size: 16px;
  color: var(--primary-color);
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats {
  display: flex;
  gap: 16px;
}

.view-count,
.like-count {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
}

.view-count .el-icon,
.like-count .el-icon {
  font-size: 16px;
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  padding: 20px;
}

.modern-pagination:deep(.el-pagination) {
  background: white;
  padding: 16px 24px;
  border-radius: 16px;
  box-shadow: var(--shadow-md);
}

.modern-pagination:deep(.el-pager li) {
  border-radius: 8px;
  min-width: 36px;
  height: 36px;
  line-height: 36px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.modern-pagination:deep(.el-pager li:hover) {
  background: rgba(0, 184, 148, 0.1);
  color: var(--primary-color);
  transform: scale(1.1);
}

.modern-pagination:deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 184, 148, 0.3);
}

.modern-pagination:deep(.el-pagination__jump) {
  margin-left: 20px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .product-list {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 16px;
  }
  
  .categories {
    justify-content: center;
  }
  
  .category-card {
    padding: 12px 16px;
    min-width: 80px;
  }
  
  .page-title {
    font-size: 24px;
  }
}
</style>
